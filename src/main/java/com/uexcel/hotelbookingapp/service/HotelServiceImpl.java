package com.uexcel.hotelbookingapp.service;

import com.uexcel.hotelbookingapp.dto.BookDto;
import com.uexcel.hotelbookingapp.dto.RoomDto;
import com.uexcel.hotelbookingapp.entity.Booked;
import com.uexcel.hotelbookingapp.entity.BookingTracker;
import com.uexcel.hotelbookingapp.entity.BookingTrackerIdClass;
import com.uexcel.hotelbookingapp.entity.Room;
import com.uexcel.hotelbookingapp.repository.BookedRepository;
import com.uexcel.hotelbookingapp.repository.BookingTrackerRepository;
import com.uexcel.hotelbookingapp.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class HotelServiceImpl implements HotelService {
    private final RoomRepository roomRepository;
    private final BookedRepository bookedRepository;

    private  final BookingTrackerRepository bookingTrackerRepository;

    public HotelServiceImpl(RoomRepository roomRepository,
                            BookedRepository bookedRepository,
                            BookingTrackerRepository bookingTrackerRepository) {
        this.roomRepository = roomRepository;
        this.bookedRepository = bookedRepository;
        this.bookingTrackerRepository = bookingTrackerRepository;
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

        if(bookDto.getBookedStartDate().getYear() != LocalDate.now().getYear()){
            return "Invalid year. The year "+ bookDto.getBookedStartDate().getYear() + " is in the past";
        }

        if(bookDto.getBookedEndDate().getYear() != LocalDate.now().getYear()){
            return "Invalid year. The year "+ bookDto.getBookedEndDate().getYear() + " is in the past";
        }

        if(bookDto.getBookedStartDate().getDayOfYear() < LocalDate.now().getDayOfYear()){
            return "Invalid date: This date "+ bookDto.getBookedStartDate() + " is in the past!";
        }

        if(bookDto.getBookedStartDate().getDayOfYear() > LocalDate.now().getDayOfYear()){
            return "Invalid date: This date "+ bookDto.getBookedEndDate() + " is in the past!";
        }

        Room room = roomRepository.findByRoomNumber(bookDto.getRoomNumber());
        if(room == null){
            return  "Invalid room number!!!";
        }

        if(room.getStatus().equals("occupied")){
            return  "The room is unavailable!!!";
        }

        int numberOfDays = (bookDto.getBookedEndDate().getDayOfYear() - bookDto.getBookedStartDate().getDayOfYear())+1;


        for(int i = 0; i < numberOfDays; i++){
             BookingTrackerIdClass bookingTrackerIdClass = new BookingTrackerIdClass(
                     LocalDate.now().getYear(),bookDto.getBookedStartDate().getDayOfYear()+i,
                     bookDto.getRoomNumber());
            Optional<BookingTracker> br = bookingTrackerRepository.findById(bookingTrackerIdClass);

            if(br.isPresent()){
                return "Status: Failed!\nThe room is reserved on this date; "+ LocalDate.ofYearDay(
                        bookingTrackerIdClass.getYear(),bookingTrackerIdClass.getDayOfYear() ) ;
            }

        }


        for(int i = 0; i < numberOfDays; i++){
            BookingTracker bookingTracker = new BookingTracker();
            bookingTracker.setYear(LocalDate.now().getYear());
            bookingTracker.setDayOfYear(bookDto.getBookedStartDate().getDayOfYear()+i);
            bookingTracker.setRoomNumber(bookDto.getRoomNumber());
            bookingTrackerRepository.save(bookingTracker);

        }


        Booked booked = new Booked();
        booked.setFirstName(bookDto.getFirstName());
        booked.setRoom(room);
        booked.setLastName(bookDto.getLastName());
        booked.setBookedStartDate(bookDto.getBookedStartDate());
        booked.setBookedEndDate(bookDto.getBookedEndDate());
        booked.setReservationNumber(bookDto.getReservationNumber());
        bookedRepository.save(booked);
        return "Booking was successfully.\n Keep your reservation number: "+ bookDto.getReservationNumber();

    }

    @Override
    public List<Room> getAvailableRooms() {
        return roomRepository.fetchAllRoom();
    }


}
