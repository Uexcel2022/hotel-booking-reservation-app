package com.uexcel.hotelbookingapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "room_number_unique",columnNames = "room_number"))
@Data
@NoArgsConstructor
public class Room {
    @Id
    @Column(name="room_number", nullable = false)
    private String roomNumber;
    private double amount;
    private  String status;

    @OneToMany(mappedBy = "room")
    private List<Booked> booked;

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}
