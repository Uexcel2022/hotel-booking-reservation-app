package com.uexcel.hotelbookingapp.service;

import com.uexcel.hotelbookingapp.dto.BookDto;
import com.uexcel.hotelbookingapp.dto.RoomDto;
import com.uexcel.hotelbookingapp.entity.Booked;
import com.uexcel.hotelbookingapp.entity.Room;
import com.uexcel.hotelbookingapp.repository.BookedRepository;
import com.uexcel.hotelbookingapp.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    private final RoomRepository roomRepository;
    private final BookedRepository bookedRepository;

    public HotelServiceImpl(RoomRepository roomRepository, BookedRepository bookedRepository) {
        this.roomRepository = roomRepository;
        this.bookedRepository = bookedRepository;
    }

    @Override
    public void savaRoom(RoomDto roomDto) {
        Room room = new Room();
        room.setRoomNumber(roomDto.getRoomNumber());
        room.setAmount(roomDto.getAmount());
        room.setStatus("available");
        roomRepository.save(room);
    }

    @Override
    public String saveBooking(BookDto bookDto) {
        Room room = roomRepository.findByRoomNumber(bookDto.getRoomNumber());
        if(room == null){
            return  "Invalid room number!!!";
        }

        if(room.getStatus().equals("booked")){
            return  "The room is unavailable!!!";
        }
        room.setStatus("booked");
        Booked booked = new Booked();
        booked.setRoom(room);
        booked.setFirstName(bookDto.getFirstName());
        booked.setLastName(bookDto.getLastName());
        booked.setBookedDate(LocalDate.now());
        booked.setReservationNumber(bookDto.getReservationNumber());
        bookedRepository.save(booked);
        return "Booking was successfully.\n Keep your reservation number: "+ bookDto.getReservationNumber();

    }

    @Override
    public List<Room> getAvailableRooms() {
        return roomRepository.fetchAllRoom();
    }
}
