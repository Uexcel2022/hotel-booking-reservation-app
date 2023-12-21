package com.uexcel.hotelbookingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class RoomCalenderModel {
   private String roomNumber;
   private LocalDate bookedDate;
   private String weekDays;
}
