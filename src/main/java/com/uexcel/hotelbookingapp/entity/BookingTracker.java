package com.uexcel.hotelbookingapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

@Entity
@IdClass(BookingTrackerIdClass.class)
@Data
public class BookingTracker {
    @Id
    private int year ;
    @Id
    private  int dayOfYear;
    @Id
    private String roomNumber;

}
