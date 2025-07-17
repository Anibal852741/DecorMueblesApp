package com.cardenas.stockifi;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ProductViewHolder> {

    private List<Product> productList;
    private final OnProductClickListener onProductClickListener;

    public InventoryAdapter(List<Product> productList, OnProductClickListener onProductClickListener) {
        this.productList = productList;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        String productName = product.getName() != null ? product.getName() : "Producto desconocido";
        int productQuantity = product.getQuantity() >= 0 ? product.getQuantity() : 0;
        double productPrice = product.getPrice() >= 0 ? product.getPrice() : 0.0;

        holder.nameTextView.setText(productName);
        holder.quantityTextView.setText("Cantidad: " + productQuantity);
        holder.priceTextView.setText("Precio: S/." + String.format("%.2f", productPrice));

        double totalPrice = productQuantity * productPrice;
        holder.totalPriceTextView.setText("Total: S/." + String.format("%.2f", totalPrice));

        // Mostrar imagen del producto
        if (product.getImageUri() != null) {
            holder.productImageView.setImageURI(Uri.parse(product.getImageUri()));
        } else {
            holder.productImageView.setImageResource(R.drawable.placeholder_image); // Asegúrate de tener una imagen por defecto
        }

        holder.itemView.setOnClickListener(v -> onProductClickListener.onProductClick(product));
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, quantityTextView, priceTextView, totalPriceTextView;
        ImageView productImageView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name);
            quantityTextView = itemView.findViewById(R.id.product_quantity);
            priceTextView = itemView.findViewById(R.id.product_price);
            totalPriceTextView = itemView.findViewById(R.id.product_total_price);
            productImageView = itemView.findViewById(R.id.product_image); // NUEVO
        }
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public void updateProductList(List<Product> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }
}
