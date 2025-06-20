package com.cardenas.stockifi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartList;
    private OnCartItemUpdatedListener onCartItemUpdatedListener;
    private OnCartItemRemovedListener onCartItemRemovedListener;

    public CartAdapter(List<CartItem> cartList,
                       OnCartItemUpdatedListener updatedListener,
                       OnCartItemRemovedListener removedListener) {
        this.cartList = cartList;
        this.onCartItemUpdatedListener = updatedListener;
        this.onCartItemRemovedListener = removedListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartList.get(position);

        holder.nameTextView.setText(cartItem.getProduct().getName());
        holder.quantityEditText.setText(String.valueOf(cartItem.getQuantity()));
        holder.totalPriceTextView.setText("Total: S/." + String.format("%.2f", cartItem.getTotalPrice()));


        holder.incrementButton.setOnClickListener(v -> {
            int newQuantity = cartItem.getQuantity() + 1;
            onCartItemUpdatedListener.onCartItemUpdated(cartItem, newQuantity);
        });

        holder.decrementButton.setOnClickListener(v -> {
            int newQuantity = cartItem.getQuantity() - 1;
            if (newQuantity > 0) {
                onCartItemUpdatedListener.onCartItemUpdated(cartItem, newQuantity);
            } else {
                onCartItemRemovedListener.onCartItemRemoved(cartItem);
            }
        });

        holder.removeButton.setOnClickListener(v -> onCartItemRemovedListener.onCartItemRemoved(cartItem));

        holder.quantityEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                int newQuantity = Integer.parseInt(holder.quantityEditText.getText().toString());
                onCartItemUpdatedListener.onCartItemUpdated(cartItem, newQuantity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, totalPriceTextView;
        EditText quantityEditText;
        Button incrementButton, decrementButton, removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cart_item_name);
            totalPriceTextView = itemView.findViewById(R.id.cart_item_total_price);
            quantityEditText = itemView.findViewById(R.id.cart_item_quantity);
            incrementButton = itemView.findViewById(R.id.increment_button);
            decrementButton = itemView.findViewById(R.id.decrement_button);
            removeButton = itemView.findViewById(R.id.remove_button);
        }
    }

    public interface OnCartItemUpdatedListener {
        void onCartItemUpdated(CartItem cartItem, int newQuantity);
    }

    public interface OnCartItemRemovedListener {
        void onCartItemRemoved(CartItem cartItem);
    }
}
