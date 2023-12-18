package com.uexcel.hotelbookingapp.controller;

import com.uexcel.hotelbookingapp.entity.Booked;
import com.uexcel.hotelbookingapp.entity.Room;
import com.uexcel.hotelbookingapp.repository.BookedRepository;
import com.uexcel.hotelbookingapp.repository.RoomRepository;
import com.uexcel.hotelbookingapp.service.HotelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class UpdateDeleteController {
   private final HotelService hotelService;
   private  final BookedRepository bookedRepository;
   private  final RoomRepository roomepository;

    public UpdateDeleteController(HotelService hotelService,
                                  BookedRepository bookedRepository, RoomRepository repository) {
        this.hotelService = hotelService;
        this.bookedRepository = bookedRepository;
        this.roomepository = repository;
    }

    @GetMapping("/booked-room")
    public String getReservationNumber(){
        return "check-reservation";
    }

    @PostMapping("/booked-room")
    public String getBookedRoom(@RequestParam("reservationNumber") String reservationNumber, Model model){
        Booked booked = hotelService.getRoom(reservationNumber);
        if(booked != null) {
            model.addAttribute("bookedInfo", booked);
            return "reservation";
        }
        model.addAttribute("error", "Invalid reservation number...");
        return "check-reservation";
    }


    @GetMapping("/checked_in")
    public String checkin(@RequestParam("reservationNumber") String bookedNumber){

        Booked booked = hotelService.getRoom(bookedNumber);
        booked.setChickIn("checkin");
        booked.setCheckInDate(LocalDate.now());

        Room room = roomepository.findByRoomNumber(booked.getRoom().getRoomNumber());
        room.setStatus("occupied");
        booked.setRoom(room);
        bookedRepository.save(booked);
        return "reservation";
    }
}
