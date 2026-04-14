package com.example.hamamlaha.models;

import androidx.annotation.NonNull;
import com.google.firebase.database.PropertyName;

public class User {
    public String fname;
    public String lname;
    public String id;
    public String email;
    public String password;
    public String phone;
    private boolean admin;  // שונה ל-private
    public String adminCategory;

    public User() {
    }

    public User(String fname, String lname, String id, String email, String password, String phone, boolean admin, String adminCategory) {
        this.fname = fname;
        this.lname = lname;
        this.id = id;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.admin = admin;
        this.adminCategory = adminCategory;
    }

    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }

    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @PropertyName("admin")
    public boolean isAdmin() { return admin; }

    @PropertyName("admin")
    public void setAdmin(boolean admin) { this.admin = admin; }

    public String getAdminCategory() { return adminCategory; }
    public void setAdminCategory(String adminCategory) { this.adminCategory = adminCategory; }

    public String getFullName() { return this.fname + " " + this.lname; }

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
                ", admin=" + admin +
                ", adminCategory='" + adminCategory + '\'' +
                '}';
    }
}