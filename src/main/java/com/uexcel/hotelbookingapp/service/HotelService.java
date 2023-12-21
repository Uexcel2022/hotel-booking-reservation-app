package com.uexcel.hotelbookingapp.service;

import com.uexcel.hotelbookingapp.dto.BookDto;
import com.uexcel.hotelbookingapp.dto.RoomCalenderModel;
import com.uexcel.hotelbookingapp.dto.RoomDto;
import com.uexcel.hotelbookingapp.entity.Booked;
import com.uexcel.hotelbookingapp.entity.Room;

import java.util.List;

public interface HotelService {
    void savaRoom(RoomDto roomDto);

    String saveBooking(BookDto bookDto,String checkin);


    List<Room> getAvailableRooms();

    Booked getRoom(String bookedNumber);

    List<Booked> getAllBookedRoom();

    void deleteReservation(String reservationNumber);

    void checkout(String reservationNumber);

    List<RoomCalenderModel> getRoomBookedDates(String roomNumber);

    String saveBookCheckin(BookDto roomNumber);

}
