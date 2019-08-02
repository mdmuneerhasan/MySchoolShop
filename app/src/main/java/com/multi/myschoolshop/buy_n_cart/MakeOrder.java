package com.multi.myschoolshop.buy_n_cart;

import com.multi.myschoolshop.shop.Item;

import java.util.ArrayList;

public class MakeOrder {
    private static final MakeOrder ourInstance = new MakeOrder();
    ArrayList<Item> arrayList;
    String name;
    String address;
    String amount;
    public static MakeOrder getInstance() {
        return ourInstance;
    }

    private MakeOrder() {
    }

    public ArrayList<Item> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Item> arrayList) {
        this.arrayList = arrayList;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
