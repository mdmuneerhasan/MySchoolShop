package com.multi.myschoolshop.utility;

public class GridClass {
    int activity;
    int res;
    String Title,Description;

    public GridClass(int activity, int res, String title, String description) {
        this.activity = activity;
        this.res = res;
        Title = title;
        Description = description;
    }

    public GridClass() {
    }

    public GridClass(int activity) {
        this.activity = activity;
    }


    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
