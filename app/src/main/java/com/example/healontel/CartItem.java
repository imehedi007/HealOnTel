package com.example.healontel;

public class CartItem {
    private String username;
    private String product;
    private String price;
    private String text;

    public CartItem() {
        // Default constructor required for Firebase
    }

    public CartItem(String username, String product, String price, String text) {
        this.username = username;
        this.product = product;
        this.price = price;
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}


