package com.multi.myschoolshop.utility;

public class TextClass {
    int switchCase;
    int res;
    String heading,title,description;

    public TextClass(int switchCase, int res, String title, String description) {
        this.switchCase = switchCase;
        this.res = res;
        this.title = title;
        this.description = description;
    }

    public TextClass() {
    }

    public TextClass(int switchCase, int res, String heading, String title, String description) {
        this.switchCase = switchCase;
        this.res = res;
        this.heading = heading;
        this.title = title;
        this.description = description;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public TextClass(int switchCase) {
        this.switchCase = switchCase;
    }


    public int getSwitchCase() {
        return switchCase;
    }

    public void setSwitchCase(int switchCase) {
        this.switchCase = switchCase;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
