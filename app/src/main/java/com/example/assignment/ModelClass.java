package com.example.assignment;

import android.graphics.Bitmap;

public class ModelClass {
    private Bitmap image;

    private String name, email, password;
    private String product_code, product_name, category, price, description;

    public ModelClass(Bitmap image, String product_code, String product_name, String category, String price, String description) {
        this.image = image;
        this.product_code = product_code;
        this.product_name = product_name;
        this.category = category;
        this.price = price;
        this.description = description;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ModelClass(String name, String email, String password) {
//        this.imageName = imageName;
//        this.image = image;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public ModelClass(String category) {
        this.category = category;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
