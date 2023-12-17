package com.uexcel.hotelbookingapp.dto;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Data
public class BookDto {
    private String roomNumber;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private String reservationNumber = UUID.randomUUID().toString();
}
