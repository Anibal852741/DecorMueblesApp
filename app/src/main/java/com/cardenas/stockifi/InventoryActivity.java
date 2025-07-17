package com.cardenas.stockifi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private List<Product> productList;
    private InventoryAdapter inventoryAdapter;
    private Product selectedProduct;
    private Uri selectedImageUri;
    private ImageView tempProductImageView;

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    if (tempProductImageView != null) {
                        tempProductImageView.setImageURI(uri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        databaseManager = new DatabaseManager(this);
        productList = databaseManager.getAllProducts();

        RecyclerView recyclerView = findViewById(R.id.inventory_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        inventoryAdapter = new InventoryAdapter(productList, product -> selectedProduct = product);
        recyclerView.setAdapter(inventoryAdapter);

        Button addButton = findViewById(R.id.add_product_button);
        addButton.setOnClickListener(v -> showProductDialog("Agregar Producto", null));

        Button editButton = findViewById(R.id.edit_product_button);
        editButton.setOnClickListener(v -> {
            if (selectedProduct != null) {
                showProductDialog("Editar Producto", selectedProduct);
            } else {
                showAlert("Selecciona un producto para editar.");
            }
        });

        Button deleteButton = findViewById(R.id.delete_product_button);
        deleteButton.setOnClickListener(v -> {
            if (selectedProduct != null) {
                databaseManager.deleteProduct(selectedProduct.getId());
                productList.remove(selectedProduct);
                inventoryAdapter.notifyDataSetChanged();
                selectedProduct = null;
            } else {
                showAlert("Selecciona un producto para eliminar.");
            }
        });
    }

    private void showProductDialog(String title, Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_product, null);
        builder.setView(dialogView);

        EditText nameInput = dialogView.findViewById(R.id.product_name_input);
        EditText quantityInput = dialogView.findViewById(R.id.product_quantity_input);
        EditText priceInput = dialogView.findViewById(R.id.product_price_input);
        tempProductImageView = dialogView.findViewById(R.id.product_image_view);
        Button imageButton = dialogView.findViewById(R.id.select_image_button);

        selectedImageUri = null;

        if (product != null) {
            nameInput.setText(product.getName());
            quantityInput.setText(String.valueOf(product.getQuantity()));
            priceInput.setText(String.valueOf(product.getPrice()));
            if (product.getImageUri() != null) {
                File imageFile = new File(product.getImageUri());
                if (imageFile.exists()) {
                    selectedImageUri = Uri.fromFile(imageFile);
                    tempProductImageView.setImageURI(selectedImageUri);
                }
            }
        }

        imageButton.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            int quantity = Integer.parseInt(quantityInput.getText().toString().trim());
            double price = Double.parseDouble(priceInput.getText().toString().trim());
            String imageUriString = null;

            if (selectedImageUri != null) {
                imageUriString = saveImageToInternalStorage(selectedImageUri);
            }

            if (product == null) {
                long newProductId = databaseManager.insertProduct(name, quantity, price, imageUriString);
                if (newProductId != -1) {
                    Product newProduct = new Product((int) newProductId, name, quantity, price, imageUriString);
                    productList.add(newProduct);
                    inventoryAdapter.notifyDataSetChanged();
                } else {
                    showAlert("Error al agregar el producto.");
                }
            } else {
                databaseManager.updateProduct(product.getId(), name, quantity, price, imageUriString);
                product.setName(name);
                product.setQuantity(quantity);
                product.setPrice(price);
                product.setImageUri(imageUriString);
                inventoryAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            File imageFile = new File(getFilesDir(), "img_" + System.currentTimeMillis() + ".jpg");
            OutputStream outputStream = new FileOutputStream(imageFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            return imageFile.getAbsolutePath(); // Ruta segura
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Atención")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
