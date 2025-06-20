package com.cardenas.stockifi;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ReportsActivity extends AppCompatActivity {

    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        databaseManager = new DatabaseManager(this);

        List<Transaction> transactions = databaseManager.getAllTransactions();

        RecyclerView recyclerView = findViewById(R.id.reports_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ReportsAdapter(transactions));
    }
}
