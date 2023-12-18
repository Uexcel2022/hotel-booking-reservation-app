package com.uexcel.hotelbookingapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Booked {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookId;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private String reservationNumber;

    private LocalDate bookedStartDate;
    private LocalDate bookedEndDate;
    private LocalDate checkInDate;
    private LocalDate checkedOutDate;


    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name="room_number",referencedColumnName = "room_number")
    private Room room;
}
