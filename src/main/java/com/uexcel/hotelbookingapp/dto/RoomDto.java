package com.uexcel.hotelbookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
@Data
public class RoomDto {
    private String roomNumber;
    private double amount;

}
