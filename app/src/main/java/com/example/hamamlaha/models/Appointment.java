package com.example.hamamlaha.models;

import androidx.annotation.NonNull;

public class Appointment {

    private String appointmentId;
    private String date;          // לדוגמה: 25/06/2025
    private String time;          // לדוגמה: 14:30
    private SalonCategory category;
    private User user;
    private String status;        // PENDING / APPROVED / CANCELED

    // חובה ל-Firebase
    public Appointment() {
    }

    public Appointment(String appointmentId,
                       String date,
                       String time,
                       SalonCategory category,
                       User user,
                       String status) {

        this.appointmentId = appointmentId;
        this.date = date;
        this.time = time;
        this.category = category;
        this.user = user;
        this.status = status;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public SalonCategory getCategory() {
        return category;
    }

    public void setCategory(SalonCategory category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NonNull
    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId='" + appointmentId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", category=" + category +
                ", user=" + user +
                ", status='" + status + '\'' +
                '}';
    }
}
