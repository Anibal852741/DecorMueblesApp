package com.cardenas.stockifi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private List<Product> productList;
    private InventoryAdapter inventoryAdapter;
    private Product selectedProduct;

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

        if (product != null) {
            nameInput.setText(product.getName());
            quantityInput.setText(String.valueOf(product.getQuantity()));
            priceInput.setText(String.valueOf(product.getPrice()));
        }

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            int quantity = Integer.parseInt(quantityInput.getText().toString().trim());
            double price = Double.parseDouble(priceInput.getText().toString().trim());

            if (product == null) {
                long newProductId = databaseManager.insertProduct(name, quantity, price);
                if (newProductId != -1) {
                    Product newProduct = new Product((int) newProductId, name, quantity, price);
                    productList.add(newProduct);
                    inventoryAdapter.notifyDataSetChanged();
                } else {
                    showAlert("Error al agregar el producto.");
                }
            } else {
                databaseManager.updateProduct(product.getId(), name, quantity, price);
                product.setName(name);
                product.setQuantity(quantity);
                product.setPrice(price);
                inventoryAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atención");
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
