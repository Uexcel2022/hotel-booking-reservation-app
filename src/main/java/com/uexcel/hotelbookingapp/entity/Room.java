package com.uexcel.hotelbookingapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "roomNumber"))
@Data
public class Room {
    @Id
    @Column(nullable = false)
    private String roomNumber;

    private double amount;

    private  Status status;

}
