package com.cardenas.stockifi;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "stockifi.db";
    private static final int DATABASE_VERSION = 4;

    // Tabla de usuarios
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";

    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_ROLE + " TEXT DEFAULT 'vendedor');";

    // Tabla de inventario
    public static final String TABLE_INVENTORY = "inventory";
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
    public static final String COLUMN_PRODUCT_PRICE = "price";

    private static final String CREATE_TABLE_INVENTORY =
            "CREATE TABLE " + TABLE_INVENTORY + " (" +
                    COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRODUCT_NAME + " TEXT, " +
                    COLUMN_PRODUCT_QUANTITY + " INTEGER, " +
                    COLUMN_PRODUCT_PRICE + " REAL);";

    // Tabla de transacciones
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COLUMN_TRANSACTION_ID = "id";
    public static final String COLUMN_TRANSACTION_DATE = "date";
    public static final String COLUMN_TRANSACTION_PRODUCT = "product_name";
    public static final String COLUMN_TRANSACTION_QUANTITY = "quantity";
    public static final String COLUMN_TRANSACTION_TOTAL = "total_price";

    private static final String CREATE_TABLE_TRANSACTIONS =
            "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                    COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TRANSACTION_DATE + " INTEGER, " +
                    COLUMN_TRANSACTION_PRODUCT + " TEXT, " +
                    COLUMN_TRANSACTION_QUANTITY + " INTEGER, " +
                    COLUMN_TRANSACTION_TOTAL + " REAL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_INVENTORY);
        db.execSQL(CREATE_TABLE_TRANSACTIONS);

        ContentValues adminValues = new ContentValues();
        adminValues.put(COLUMN_NAME, "Admin");
        adminValues.put(COLUMN_EMAIL, "admin@example.com");
        adminValues.put(COLUMN_PASSWORD, "admin123");
        adminValues.put(COLUMN_ROLE, "admin");
        db.insert(TABLE_USERS, null, adminValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(CREATE_TABLE_INVENTORY);
        }
        if (oldVersion < 3) {
            db.execSQL(CREATE_TABLE_TRANSACTIONS);
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_ROLE + " TEXT DEFAULT 'vendedor';");
        }
    }
}
