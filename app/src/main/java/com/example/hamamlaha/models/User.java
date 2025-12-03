package com.example.hamamlaha.models;

import androidx.annotation.NonNull;

public class User
{
    public String fname;
    public String lname;
    public String id;
    public String email;
    public String password;
    public String phone;

    public boolean isAdmin;

    public User() {
    }

    public User(String fname, String lname, String id, String email, String password, String phone, boolean isAdmin) {
        this.fname = fname;
        this.lname = lname;
        this.id = id;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.isAdmin = isAdmin;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
