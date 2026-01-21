package com.example.hamamlaha.models;

import java.time.LocalTime;
import java.util.List;

public class SubSalon {

    private String id;
    private String name;
    private String category;
    private List<Appointment> appointments;

    private LocalTime openingTime;
    private LocalTime closingTime;

}
