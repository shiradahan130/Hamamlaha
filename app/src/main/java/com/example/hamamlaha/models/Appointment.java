package com.example.hamamlaha.models;

import androidx.annotation.NonNull;

public class Appointment {

    private String appointmentId;
    private String date;
    private String time;
    private SalonCategory category;
    private String userId;
    private String status;
    private int duration; // אורך התור בשעות

    // חובה ל-Firebase
    public Appointment() {
    }

    public Appointment(String appointmentId,
                       String date,
                       String time,
                       SalonCategory category,
                       String userId,
                       String status) {
        this.appointmentId = appointmentId;
        this.date = date;
        this.time = time;
        this.category = category;
        this.userId = userId;
        this.status = status;
        this.duration = 1; // ברירת מחדל - שעה אחת
    }

    public Appointment(String appointmentId,
                       String date,
                       String time,
                       SalonCategory category,
                       String userId,
                       String status,
                       int duration) {
        this.appointmentId = appointmentId;
        this.date = date;
        this.time = time;
        this.category = category;
        this.userId = userId;
        this.status = status;
        this.duration = duration;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @NonNull
    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId='" + appointmentId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", category=" + category +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                ", duration=" + duration +
                '}';
    }
}