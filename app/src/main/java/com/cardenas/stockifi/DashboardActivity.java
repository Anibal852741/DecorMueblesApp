package com.cardenas.stockifi;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private PieChart pieChart;
    private BarChart barChart;
    private DatabaseManager databaseManager;
    private LinearLayout barLegendContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        databaseManager = new DatabaseManager(this);

        pieChart = findViewById(R.id.pie_chart);
        barChart = findViewById(R.id.bar_chart);
        barLegendContainer = findViewById(R.id.bar_legend_container);

        setupPieChart();       
        setupBarChart();
    }

    private void setupPieChart() {
        List<String> productNames = databaseManager.getTopSellingProducts();
        List<PieEntry> entries = new ArrayList<>();

        for (String product : productNames) {
            int sales = databaseManager.getSalesForProduct(product);
            entries.add(new PieEntry(sales, product));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Productos Más Vendidos");
        dataSet.setColors(new int[]{
                ContextCompat.getColor(this, R.color.blue),
                ContextCompat.getColor(this, R.color.green),
                ContextCompat.getColor(this, R.color.orange),
                ContextCompat.getColor(this, R.color.red),
                ContextCompat.getColor(this, R.color.purple)
        });
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Ventas");
        pieChart.setCenterTextSize(16f);
        pieChart.animateY(1400);
        pieChart.invalidate();
    }

    private void setupBarChart() {
        List<String> productNames = databaseManager.getAllProductNames();
        List<Integer> stockQuantities = databaseManager.getAllProductQuantities();

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < productNames.size(); i++) {
            entries.add(new BarEntry(i, stockQuantities.get(i)));
            labels.add(productNames.get(i));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Stock por Producto");
        barDataSet.setColor(ContextCompat.getColor(this, R.color.blue));
        barDataSet.setValueTextSize(12f);
        barDataSet.setValueTextColor(Color.DKGRAY);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setFitBars(true);
        barChart.animateY(1400);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.size());
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= 0 && value < labels.size()) {
                    return labels.get((int) value);
                }
                return "";
            }
        });

        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisRight().setEnabled(false);
        barChart.invalidate();
    }

    private void setupBarChartLegend(List<String> labels, List<Integer> colors) {
        barLegendContainer.removeAllViews();

        for (int i = 0; i < labels.size(); i++) {
            TextView legendItem = new TextView(this);
            legendItem.setText("◼ " + labels.get(i));
            legendItem.setTextSize(14f);
            legendItem.setTextColor(colors.get(i));
            legendItem.setPadding(4, 4, 4, 4);
            barLegendContainer.addView(legendItem);
        }
    }
}
