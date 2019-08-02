package com.multi.myschoolshop.fragment;

public class SetUpFragmentHelperClass {
    String url,name,extra,button,id;


    public SetUpFragmentHelperClass() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getButton() {
        return button;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SetUpFragmentHelperClass(String url, String name, String extra, String button) {
        this.url = url;
        this.name = name;
        this.extra = extra;
        this.button = button;
    }

    public SetUpFragmentHelperClass(String url) {
        this.url = url;
    }
}
