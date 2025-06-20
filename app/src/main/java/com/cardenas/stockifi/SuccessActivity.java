package com.cardenas.stockifi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        TextView successText = findViewById(R.id.success_message);
        successText.setText("¡Venta realizada con éxito!");

        Button newSaleButton = findViewById(R.id.new_sale_button);
        Button goToMenuButton = findViewById(R.id.go_to_menu_button);

        newSaleButton.setOnClickListener(v -> {
            Intent intent = new Intent(SuccessActivity.this, StockActivity.class);
            startActivity(intent);
            finish();
        });

        String role = getIntent().getStringExtra("user_role");

        goToMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
            intent.putExtra("user_role", role);
            startActivity(intent);
            finish();
        });


    }
}
