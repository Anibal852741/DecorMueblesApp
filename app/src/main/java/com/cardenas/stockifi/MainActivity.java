package com.cardenas.stockifi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDelegate;



import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Forzar modo claro
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button stockButton = findViewById(R.id.stock_button);
        Button inventoryButton = findViewById(R.id.inventory_button);
        Button reportsButton = findViewById(R.id.reports_button);
        Button usersButton = findViewById(R.id.users_button);
        Button dashboardButton = findViewById(R.id.dashboard_button);
        Button logoutButton = findViewById(R.id.logout_button);

        // Obtener el rol del usuario desde el Intent
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        String userRole = prefs.getString("user_role", "admin"); // "admin" por defecto

        if ("vendedor".equalsIgnoreCase(userRole)) {
            usersButton.setVisibility(View.GONE);
            inventoryButton.setVisibility(View.GONE);
        }



        // Configurar listeners para cada botón
        stockButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StockActivity.class);
            startActivity(intent);
        });

        inventoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
            startActivity(intent);
        });


        reportsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportsActivity.class);
            startActivity(intent);
        });

        usersButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UsersActivity.class);
            startActivity(intent);
        });

        dashboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            // Limpiar datos de sesión
            getSharedPreferences("user_session", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            // Redirigir al Login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Elimina historial
            startActivity(intent);
            finish();
        });


        TextView productsCountTextView = findViewById(R.id.products_count);
        TextView salesCountTextView = findViewById(R.id.recent_sales);
        DatabaseManager dbManager = new DatabaseManager(this);

// Productos en stock
        int inventoryCount = dbManager.getInventoryCount();
        productsCountTextView.setText(String.valueOf(inventoryCount));

// Ventas del día
        int todaySales = dbManager.getTodaySalesCount();
        salesCountTextView.setText(String.valueOf(todaySales));



    }
}
