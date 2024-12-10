package com.example.bezimyannoe;

public class Item {
    private int id;
    private String title;
    private double price;
    private String photoPath;

    // Constructor
    public Item(int id, String title, double price, String photoPath) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.photoPath = photoPath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}