package com.uexcel.hotelbookingapp.entity;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class BookingTrackerIdClass implements Serializable {
    protected int year;

    protected  int dayOfYear;

    protected String roomNumber;

    public BookingTrackerIdClass() {
    }

    public BookingTrackerIdClass(int year, int dayOfYear, String roomNumber) {
        this.year = year;
        this.dayOfYear = dayOfYear;
        this.roomNumber = roomNumber;
    }
}
