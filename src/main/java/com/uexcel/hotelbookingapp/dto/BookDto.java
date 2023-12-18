package com.uexcel.hotelbookingapp.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;
@Data
public class BookDto {
    private String roomNumber;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private LocalDate bookedStartDate;
    private LocalDate bookedEndDate;
    private String reservationNumber = UUID.randomUUID().toString();
}
