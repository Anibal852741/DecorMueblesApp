package com.cardenas.stockifi;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private String imageUri;

    public Product(int id, String name, int quantity, double price, String imageUri) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.imageUri = imageUri;
    }

    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        quantity = in.readInt();
        price = in.readDouble();
        imageUri = in.readString(); // LECTURA NUEVO CAMPO
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getQuantity() {
        return quantity;
    }
    public double getPrice() {
        return price;
    }
    public String getImageUri() {
        return imageUri;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeDouble(price);
        dest.writeString(imageUri); // ESCRITURA NUEVO CAMPO
    }
}
