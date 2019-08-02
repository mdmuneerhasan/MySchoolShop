package com.multi.myschoolshop.utility;
public class User {
    String name;
    String stClass;
    String position;
    String schoolName,schoolId;
    String uid,sid;
    String number;
    String photoUrl,email,rollNo;

    public User(String name, String stClass, String position, String schoolName, String uid, String number, String url) {
        this.name = name;
        this.stClass = stClass;
        this.position = position;
        this.schoolName = schoolName;
        this.uid = uid;
        this.number = number;
        this.photoUrl=url;
    }


    public User(String name, String uid, String photoUrl) {
        this.name = name;
        this.uid = uid;
        this.photoUrl = photoUrl;
    }

    public User() {
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getSid() {
        return sid;
    }

   public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStClass() {
        return stClass;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setStClass(String stClass) {
        this.stClass = stClass;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
