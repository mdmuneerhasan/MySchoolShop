package com.multi.myschoolshop.buy_n_cart;

import com.multi.myschoolshop.shop.Item;

import java.util.ArrayList;

public class Order {
    ArrayList<Item> arrayList;
    String address,name,amount,uid,time,key,schoolId,dateKey,status;
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public Order() {
    }

    public Order(ArrayList<Item> arrayList, String address, String name, String amount, String uid) {
        this.arrayList = arrayList;
        this.address = address;
        this.name = name;
        this.amount = amount;
        this.uid = uid;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(name+" ");
        try{
            for(Item item:arrayList){
                stringBuilder.append(item.getName()+" ");
            }
        }catch (Exception e){}
        return stringBuilder.toString();

    }

    public String getDateKey() {
        return dateKey;
    }

    public void setDateKey(String dateKey) {
        this.dateKey = dateKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public Order(ArrayList<Item> arrayList) {
        this.arrayList=arrayList;
    }

    public ArrayList<Item> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Item> arrayList) {
        this.arrayList = arrayList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
