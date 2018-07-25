package com.example.tomato.lesson6sqlitetdgiang.model;

public class Contact {
    private int mId;
    private String mName;
    private String mNumberPhone;

    public Contact() {
    }

    public Contact(String name, String numberPhone) {
        mName = name;
        mNumberPhone = numberPhone;
    }

    public Contact(int id, String name, String numberPhone) {
        mId = id;
        mName = name;
        mNumberPhone = numberPhone;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getNumberPhone() {
        return mNumberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        mNumberPhone = numberPhone;
    }
}
