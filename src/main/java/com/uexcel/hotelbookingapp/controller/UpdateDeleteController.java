package com.uexcel.hotelbookingapp.controller;

import com.uexcel.hotelbookingapp.entity.Booked;
import com.uexcel.hotelbookingapp.entity.Room;
import com.uexcel.hotelbookingapp.repository.BookedRepository;
import com.uexcel.hotelbookingapp.repository.RoomRepository;
import com.uexcel.hotelbookingapp.service.HotelService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class UpdateDeleteController {
   private final HotelService hotelService;
   private  final BookedRepository bookedRepository;
   private  final RoomRepository roomrepository;

    public UpdateDeleteController(HotelService hotelService,
                                  BookedRepository bookedRepository, RoomRepository repository) {
        this.hotelService = hotelService;
        this.bookedRepository = bookedRepository;
        this.roomrepository = repository;
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
        booked.setCheckIn("check_in");
        booked.setCheckInDate(LocalDate.now());

        Room room = roomrepository.findByRoomNumber(booked.getRoom().getRoomNumber());
        room.setStatus("occupied");
        booked.setRoom(room);
        bookedRepository.save(booked);
        return "reservation";
    }

    @GetMapping("/reservations")
    public String getReservations(Model model){
        List<Booked> bookedList = hotelService.getAllBookedRoom();
        model.addAttribute("bookedInfo",bookedList);
        return "reservations";

    }

    @GetMapping("/delete")
    public void deleteReservation(
            @RequestParam("reservationNumber") String reservationNumber,
            HttpServletResponse response) throws IOException {
        hotelService.deleteReservation(reservationNumber);
        response.sendRedirect("/reservations");
    }
}
