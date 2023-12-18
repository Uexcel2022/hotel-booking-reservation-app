package com.uexcel.hotelbookingapp.service;

import com.uexcel.hotelbookingapp.dto.BookDto;
import com.uexcel.hotelbookingapp.dto.RoomDto;
import com.uexcel.hotelbookingapp.entity.Booked;
import com.uexcel.hotelbookingapp.entity.Room;

import java.util.List;

public interface HotelService {
    void savaRoom(RoomDto roomDto);

    String saveBooking(BookDto bookDto);


    List<Room> getAvailableRooms();

    Booked getRoom(String bookedNumber);

    List<Booked> getAllBookedRoom();

    String deleteReservation(String reservationNumber);
}
