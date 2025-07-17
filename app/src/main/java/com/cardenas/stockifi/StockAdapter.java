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

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private List<Product> productList;
    private final OnProductClickListener onProductClickListener;

    public StockAdapter(List<Product> productList, OnProductClickListener onProductClickListener) {
        this.productList = productList;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.nameTextView.setText(product.getName());
        holder.quantityTextView.setText("Stock: " + product.getQuantity());
        holder.priceTextView.setText("Precio: S/." + String.format("%.2f", product.getPrice()));

        // Mostrar imagen si existe, si no usar la por defecto
        if (product.getImageUri() != null) {
            try {
                holder.imageView.setImageURI(Uri.parse(product.getImageUri()));
            } catch (Exception e) {
                holder.imageView.setImageResource(R.drawable.placeholder_image);
            }
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder_image);
        }

        holder.itemView.setOnClickListener(v -> onProductClickListener.onProductClick(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, quantityTextView, priceTextView;
        ImageView imageView;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name);
            quantityTextView = itemView.findViewById(R.id.product_quantity);
            priceTextView = itemView.findViewById(R.id.product_price);
            imageView = itemView.findViewById(R.id.product_image);
        }
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }
}
