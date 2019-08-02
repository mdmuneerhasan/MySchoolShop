package com.multi.myschoolshop.shop;

import java.util.ArrayList;

public class Item {
    String name,description,price,key,shopId,category,quantity;
    ArrayList<String> imagesList;
    ArrayList<Specification> arrayList;

    public Item(String name, String description, String price, ArrayList<String> images, ArrayList<Specification> arrayList) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imagesList = images;
        this.arrayList = arrayList;
    }

    @Override
    public String toString() {
        return name;
    }

    public Item() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShopId() {
        return shopId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Item(String key, String shopId) {
        this.key = key;
        this.shopId = shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(ArrayList<String> imagesList) {
        this.imagesList = imagesList;
    }

    public ArrayList<Specification> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Specification> arrayList) {
        this.arrayList = arrayList;
    }
}
