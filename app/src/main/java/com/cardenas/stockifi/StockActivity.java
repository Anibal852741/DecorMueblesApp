package com.cardenas.stockifi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StockActivity extends AppCompatActivity {
    private DatabaseManager databaseManager;
    private List<Product> productList;
    private StockAdapter stockAdapter;
    private List<CartItem> cart;
    private Button sellButton;
    private int totalItems = 0;
    private double totalPrice = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        databaseManager = new DatabaseManager(this);
        productList = databaseManager.getAllProducts();
        cart = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.stock_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        stockAdapter = new StockAdapter(productList, this::addToCart);
        recyclerView.setAdapter(stockAdapter);

        sellButton = findViewById(R.id.sell_button);
        updateSellButtonText();

        sellButton.setOnClickListener(v -> {
            Intent intent = new Intent(StockActivity.this, CartActivity.class);
            intent.putExtra("cart", new ArrayList<>(cart));
            startActivity(intent);
        });
    }
    private void addToCart(Product product) {
        boolean productExists = false;
        for (CartItem item : cart) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                productExists = true;
                break;
            }
        }
        if (!productExists) {
            cart.add(new CartItem(product, 1));
        }
        totalItems++;
        totalPrice += product.getPrice();
        updateSellButtonText();
    }
    private void updateSellButtonText() {
        sellButton.setText("Ir al carrito (" + totalItems + " items, S/." + String.format("%.2f", totalPrice) + ")");
    }
}
