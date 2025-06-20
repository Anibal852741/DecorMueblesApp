package com.cardenas.stockifi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private List<CartItem> cart;
    private CartAdapter cartAdapter;
    private TextView totalTextView;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        databaseManager = new DatabaseManager(this);

        cart = (List<CartItem>) getIntent().getSerializableExtra("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        RecyclerView recyclerView = findViewById(R.id.cart_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        totalTextView = findViewById(R.id.total_price_text_view);

        cartAdapter = new CartAdapter(cart, this::updateCartItem, this::removeCartItem);
        recyclerView.setAdapter(cartAdapter);


        updateTotalPrice();

        Button confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> confirmSale());
    }

    private void updateCartItem(CartItem cartItem, int newQuantity) {
        cartItem.setQuantity(newQuantity);
        updateTotalPrice();
        cartAdapter.notifyDataSetChanged();
    }

    private void removeCartItem(CartItem cartItem) {
        cart.remove(cartItem);
        updateTotalPrice();
        cartAdapter.notifyDataSetChanged();
    }

    private void updateTotalPrice() {
        double total = 0.0;
        for (CartItem item : cart) {
            total += item.getQuantity() * item.getProduct().getPrice();
        }
        totalTextView.setText("Total: S/." + String.format("%.2f", total));
    }


    private void confirmSale() {
        for (CartItem item : cart) {
            Product product = item.getProduct();
            int newStock = product.getQuantity() - item.getQuantity();
            if (newStock >= 0) {
                product.setQuantity(newStock);
                databaseManager.updateProduct(product.getId(), product.getName(), newStock, product.getPrice());

                databaseManager.insertTransaction(
                        System.currentTimeMillis(),
                        product.getName(),
                        item.getQuantity(),
                        item.getTotalPrice()
                );
            }
        }

        cart.clear();

        Intent intent = new Intent(CartActivity.this, SuccessActivity.class);
        intent.putExtra("user_role", getIntent().getStringExtra("user_role"));
        startActivity(intent);
        finish();


    }

}
