package com.example.healontel;

import java.util.Arrays;

public class OrderItem {
    private String username;
    private String product;
    private String price;
    private String category;
    private String date;
    private String time;
    private String name;
    private String address;
    private String contact;
    private int pincode;

    public OrderItem() {

    }


    public OrderItem(String username, String product, String price, String category, String date, String time, String name, String address, String contact, int pincode) {
        this.username = username;
        this.product = product;
        this.price = price;
        this.category = category;
        this.date = date;
        this.time = time;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.pincode = pincode;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }
}
