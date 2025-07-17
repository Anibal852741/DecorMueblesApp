package com.cardenas.stockifi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseManager {
    private final Context context;
    private final SQLiteDatabase database;
    private final DatabaseHelper dbHelper;

    public DatabaseManager(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
        this.database = dbHelper.getWritableDatabase();
    }

    public void insertUser(String name, String email, String password, String role) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        values.put(DatabaseHelper.COLUMN_ROLE, role);
        database.insert(DatabaseHelper.TABLE_USERS, null, values);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROLE));
                users.add(new User(id, name, email, password, role));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    public User getUserByEmailAndPassword(String email, String password) {
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS + " WHERE " +
                DatabaseHelper.COLUMN_EMAIL + "=? AND " + DatabaseHelper.COLUMN_PASSWORD + "=?";
        Cursor cursor = database.rawQuery(query, new String[]{email, password});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            String role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROLE));
            cursor.close();
            return new User(id, name, email, password, role);
        }
        cursor.close();
        return null;
    }

    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS, null, DatabaseHelper.COLUMN_ROLE + "=?",
                new String[]{role}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
                users.add(new User(id, name, email, password, role));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    public void updateUser(int id, String name, String email, String password, String role) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        values.put(DatabaseHelper.COLUMN_ROLE, role);
        database.update(DatabaseHelper.TABLE_USERS, values, DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public String getUserRole(String email) {
        String role = null;
        String query = "SELECT " + DatabaseHelper.COLUMN_ROLE + " FROM " + DatabaseHelper.TABLE_USERS +
                " WHERE " + DatabaseHelper.COLUMN_EMAIL + "=?";
        Cursor cursor = database.rawQuery(query, new String[]{email});
        if (cursor.moveToFirst()) {
            role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROLE));
        }
        cursor.close();
        return role;
    }

    public void deleteUser(int id) {
        database.delete(DatabaseHelper.TABLE_USERS, DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public String validateUser(String email, String password) {
        String query = "SELECT " + DatabaseHelper.COLUMN_ROLE + " FROM " + DatabaseHelper.TABLE_USERS +
                " WHERE " + DatabaseHelper.COLUMN_EMAIL + "=? AND " + DatabaseHelper.COLUMN_PASSWORD + "=?";
        Cursor cursor = database.rawQuery(query, new String[]{email, password});
        String role = null;
        if (cursor.moveToFirst()) {
            role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROLE));
        }
        cursor.close();
        return role;
    }

    public long insertProduct(String name, int quantity, double price, String imageUri) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_NAME, name);
        values.put(DatabaseHelper.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(DatabaseHelper.COLUMN_PRODUCT_PRICE, price);
        values.put(DatabaseHelper.COLUMN_PRODUCT_IMAGE_URI, imageUri);
        return database.insert(DatabaseHelper.TABLE_INVENTORY, null, values);
    }


    public void updateProduct(int id, String name, int quantity, double price, String imageUri) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_NAME, name);
        values.put(DatabaseHelper.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(DatabaseHelper.COLUMN_PRODUCT_PRICE, price);
        values.put(DatabaseHelper.COLUMN_PRODUCT_IMAGE_URI, imageUri);
        database.update(DatabaseHelper.TABLE_INVENTORY, values, DatabaseHelper.COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(id)});
    }



    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_INVENTORY, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_QUANTITY));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRODUCT_PRICE));
                String imageUri = null;
                int imageIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCT_IMAGE_URI);
                if (imageIndex >= 0) {
                    imageUri = cursor.getString(imageIndex);
                }
                products.add(new Product(id, name, quantity, price, imageUri));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }


    public void updateProduct(int id, String name, int quantity, double price) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PRODUCT_NAME, name);
        values.put(DatabaseHelper.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(DatabaseHelper.COLUMN_PRODUCT_PRICE, price);
        database.update(DatabaseHelper.TABLE_INVENTORY, values, DatabaseHelper.COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteProduct(int id) {
        database.delete(DatabaseHelper.TABLE_INVENTORY, DatabaseHelper.COLUMN_PRODUCT_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void insertTransaction(long date, String productName, int quantity, double totalPrice) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TRANSACTION_DATE, date);
        values.put(DatabaseHelper.COLUMN_TRANSACTION_PRODUCT, productName);
        values.put(DatabaseHelper.COLUMN_TRANSACTION_QUANTITY, quantity);
        values.put(DatabaseHelper.COLUMN_TRANSACTION_TOTAL, totalPrice);
        database.insert(DatabaseHelper.TABLE_TRANSACTIONS, null, values);
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_TRANSACTIONS, null, null, null, null, null,
                DatabaseHelper.COLUMN_TRANSACTION_DATE + " DESC");
        if (cursor.moveToFirst()) {
            do {
                long date = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_DATE));
                String productName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_PRODUCT));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_QUANTITY));
                double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_TOTAL));
                transactions.add(new Transaction(date, productName, quantity, totalPrice));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return transactions;
    }

    public List<String> getTopSellingProducts() {
        List<String> topSellingProducts = new ArrayList<>();
        String query = "SELECT " + DatabaseHelper.COLUMN_TRANSACTION_PRODUCT + ", SUM(" +
                DatabaseHelper.COLUMN_TRANSACTION_QUANTITY + ") as total_sold FROM " + DatabaseHelper.TABLE_TRANSACTIONS +
                " GROUP BY " + DatabaseHelper.COLUMN_TRANSACTION_PRODUCT +
                " ORDER BY total_sold DESC LIMIT 5";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String productName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_PRODUCT));
                topSellingProducts.add(productName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return topSellingProducts;
    }

    public int getSalesForProduct(String productName) {
        int totalSales = 0;
        String query = "SELECT SUM(" + DatabaseHelper.COLUMN_TRANSACTION_QUANTITY + ") as total_sold FROM " +
                DatabaseHelper.TABLE_TRANSACTIONS + " WHERE " + DatabaseHelper.COLUMN_TRANSACTION_PRODUCT + " = ?";
        Cursor cursor = database.rawQuery(query, new String[]{productName});
        if (cursor.moveToFirst()) {
            totalSales = cursor.getInt(cursor.getColumnIndexOrThrow("total_sold"));
        }
        cursor.close();
        return totalSales;
    }

    public int getTotalSales() {
        int totalSales = 0;
        String query = "SELECT SUM(" + DatabaseHelper.COLUMN_TRANSACTION_QUANTITY + ") as total_sold FROM " + DatabaseHelper.TABLE_TRANSACTIONS;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            totalSales = cursor.getInt(cursor.getColumnIndexOrThrow("total_sold"));
        }
        cursor.close();
        return totalSales;
    }

    public int getTotalStock() {
        int totalStock = 0;
        String query = "SELECT SUM(" + DatabaseHelper.COLUMN_PRODUCT_QUANTITY + ") as total_stock FROM " + DatabaseHelper.TABLE_INVENTORY;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            totalStock = cursor.getInt(cursor.getColumnIndexOrThrow("total_stock"));
        }
        cursor.close();
        return totalStock;
    }

    public List<Integer> getDailySalesLastMonth() {
        List<Integer> dailySales = new ArrayList<>();
        long currentTimeMillis = System.currentTimeMillis();
        long lastMonthMillis = currentTimeMillis - (30L * 24 * 60 * 60 * 1000);
        String query = "SELECT strftime('%d', datetime(" + DatabaseHelper.COLUMN_TRANSACTION_DATE +
                " / 1000, 'unixepoch')) AS day, SUM(" + DatabaseHelper.COLUMN_TRANSACTION_QUANTITY + ") AS total_sales FROM " +
                DatabaseHelper.TABLE_TRANSACTIONS + " WHERE " + DatabaseHelper.COLUMN_TRANSACTION_DATE + " BETWEEN ? AND ? " +
                "GROUP BY day ORDER BY day ASC";
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(lastMonthMillis), String.valueOf(currentTimeMillis)});
        while (cursor.moveToNext()) {
            int totalSales = cursor.getInt(cursor.getColumnIndexOrThrow("total_sales"));
            dailySales.add(totalSales);
        }
        cursor.close();
        return dailySales;
    }

    public int getInventoryCount() {
        int count = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(quantity) FROM inventory", null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public int getTodaySalesCount() {
        int count = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Cursor cursor = db.rawQuery("SELECT SUM(quantity) FROM transactions WHERE date LIKE ?", new String[]{today + "%"});
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public List<String> getAllProductNames() {
        List<String> names = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM " + DatabaseHelper.TABLE_INVENTORY, null);
        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return names;
    }

    public List<Integer> getAllProductQuantities() {
        List<Integer> quantities = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT quantity FROM " + DatabaseHelper.TABLE_INVENTORY, null);
        if (cursor.moveToFirst()) {
            do {
                quantities.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return quantities;
    }
}
