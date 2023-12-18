package com.uexcel.hotelbookingapp.entity;

import java.io.Serializable;

public class BookingTrackerIdClass implements Serializable {
    protected int year;

    protected  int dayOfYear;

    protected String roomNumber;

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getYear() {
        return year;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public BookingTrackerIdClass() {
    }

    public BookingTrackerIdClass(int year, int dayOfYear, String roomNumber) {
        this.year = year;
        this.dayOfYear = dayOfYear;
        this.roomNumber = roomNumber;
    }
}
