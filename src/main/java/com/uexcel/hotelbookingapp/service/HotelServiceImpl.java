package com.uexcel.hotelbookingapp.service;

import com.uexcel.hotelbookingapp.dto.BookDto;
import com.uexcel.hotelbookingapp.dto.RoomCalenderModel;
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
    public String saveBooking(BookDto bookDto, String checkin) {

        if(bookDto.getBookedStartDate().getYear() < LocalDate.now().getYear()){
            return "Invalid year. The year "+ bookDto.getBookedStartDate().getYear() + " is in the past";
        }

        if(bookDto.getBookedEndDate().getYear() < LocalDate.now().getYear()){
            return "Invalid year. The year "+ bookDto.getBookedEndDate().getYear() + " is in the past";
        }

        if(bookDto.getBookedStartDate().getDayOfYear() < LocalDate.now().getDayOfYear()){
            return "Invalid date: This date "+ bookDto.getBookedStartDate() + " is in the past!";
        }

        if(bookDto.getBookedEndDate().getDayOfYear() < LocalDate.now().getDayOfYear()){
            return "Invalid date: This date "+ bookDto.getBookedEndDate() + " is in the past!";
        }

        Room room = roomRepository.findByRoomNumber(bookDto.getRoomNumber());
        if(room == null){
            return  "Invalid room number!!!";

        }
        Booked obj = bookedRepository.getLastBooking(bookDto.getRoomNumber());
        if(room.getStatus().equals("occupied") && obj != null &&
                obj.getBookedEndDate().getDayOfYear() > bookDto.getBookedStartDate().getDayOfYear()){
            return  "Status: failed!!\nThe room is not available for booking on the date(s) you selected!";
            //write fetch the last in date and check if end date is less thank booking start date;
        }

        List<BookingTracker> bookingTrackers = bookingTrackerRepository.findAll();
        for(BookingTracker n: bookingTrackers ){
            if(LocalDate.now().getDayOfYear() > n.getDayOfYear()){
                bookingTrackerRepository.delete(n);
            }
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
        if(checkin != null){
            Room rm = new Room();
            room.setStatus("occupied");
           booked.setCheckIn(checkin);
           booked.setCheckInDate(LocalDate.now());
           booked.setRoom(rm);
        }

        booked.setFirstName(bookDto.getFirstName());
        booked.setRoom(room);
        booked.setLastName(bookDto.getLastName());
        booked.setBookedStartDate(bookDto.getBookedStartDate());
        booked.setBookedEndDate(bookDto.getBookedEndDate());
        booked.setReservationNumber(bookDto.getReservationNumber());
        booked.setBookedDate(LocalDate.now());
        bookedRepository.save(booked);
        return "Booking was successful.\n Keep your reservation number: "+ bookDto.getReservationNumber();

    }

    @Override
    public List<Room> getAvailableRooms() {
        return roomRepository.fetchAllRoom();
    }


    @Override
    public Booked getRoom(String reservationNumber) {
        return bookedRepository.findByReservationNumber(reservationNumber);
    }

    @Override
    public List<Booked> getAllBookedRoom() {
        return bookedRepository.fetchAll();

    }

    @Override
    public void deleteReservation(String reservationNumber) {
        Booked booked = bookedRepository.findByReservationNumber(reservationNumber);
        if(booked ==null){
            return;
        }
        deleteBookingTracker(booked.getBookedStartDate(), booked.getBookedEndDate(), booked.getRoom().getRoomNumber());
        bookedRepository.delete(booked);
    }

    @Override
    public void checkout(String reservationNumber) {



    }

    @Override
    public List<RoomCalenderModel> getRoomBookedDates(String roomNumber) {
        List<BookingTracker> bookingTracker =
                bookingTrackerRepository.findByRoomNumber(roomNumber);
        List<RoomCalenderModel> roomCalenderModelList = new ArrayList<>();
        if(!bookingTracker.isEmpty()){
            for(BookingTracker n: bookingTracker){
                LocalDate bookDate =
                        LocalDate.ofYearDay(LocalDate.now().getYear(),n.getDayOfYear());
                roomCalenderModelList.add(
                        new RoomCalenderModel(roomNumber, bookDate, bookDate.getDayOfWeek().toString())
                );
            }
            return roomCalenderModelList;
        }

        return roomCalenderModelList;
    }

    @Override
    public String saveBookCheckin(BookDto bookDto) {
        String checkin = "check_in";
        return saveBooking(bookDto, checkin);
    }


    private void deleteBookingTracker(LocalDate bookedStartDate, LocalDate bookedEndDate, String roomNumber) {
        int numberOfDays = bookedEndDate.getDayOfYear() - bookedStartDate.getDayOfYear() +1;
        for(int i = 0; i < numberOfDays; i++){
            BookingTrackerIdClass bookingTrackerIdClass = new BookingTrackerIdClass(
                    LocalDate.now().getYear(), bookedStartDate.getDayOfYear()+i, roomNumber);
         Optional<BookingTracker> bookingTracker =  bookingTrackerRepository.findById(bookingTrackerIdClass);
            bookingTracker.ifPresent(bookingTrackerRepository::delete);
        }

    }


}
