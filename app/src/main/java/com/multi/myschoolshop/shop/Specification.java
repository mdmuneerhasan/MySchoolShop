package com.multi.myschoolshop.shop;

public  class Specification {
    String field,value;

    public Specification() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Specification(String field, String value) {
        this.field = field;
        this.value = value;
    }
}