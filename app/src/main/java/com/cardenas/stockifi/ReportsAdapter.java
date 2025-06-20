package com.cardenas.stockifi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportViewHolder> {

    private List<Transaction> transactions;

    public ReportsAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.productName.setText(transaction.getProductName());
        holder.quantity.setText("Cantidad: " + transaction.getQuantity());
        holder.totalPrice.setText("Total: $" + String.format("%.2f", transaction.getTotalPrice()));
        holder.date.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(transaction.getDate()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView productName, quantity, totalPrice, date;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.report_product_name);
            quantity = itemView.findViewById(R.id.report_quantity);
            totalPrice = itemView.findViewById(R.id.report_total_price);
            date = itemView.findViewById(R.id.report_date);
        }
    }
}
