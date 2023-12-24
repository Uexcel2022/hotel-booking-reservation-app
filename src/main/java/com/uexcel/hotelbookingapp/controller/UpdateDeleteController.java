package com.uexcel.hotelbookingapp.controller;

import com.uexcel.hotelbookingapp.dto.BookDto;
import com.uexcel.hotelbookingapp.dto.RoomCalenderModel;
import com.uexcel.hotelbookingapp.entity.Booked;
import com.uexcel.hotelbookingapp.entity.Room;
import com.uexcel.hotelbookingapp.repository.BookedRepository;
import com.uexcel.hotelbookingapp.repository.RoomRepository;
import com.uexcel.hotelbookingapp.service.HotelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    public String getBookedRoom(
            @RequestParam("reservationNumber") String reservationNumber, Model model){
        Booked booked = hotelService.getRoom(reservationNumber);
        if(booked != null) {
            model.addAttribute("bookedInfo", booked);
            return "reservation";
        }
        model.addAttribute("error", "Invalid reservation number...");
        return "check-reservation";
    }

    @GetMapping(value = {"/checked_in","/checkin"})
    public String checkin(
            @RequestParam("reservationNumber") String reservationNumber,
            Model model,HttpServletResponse response, HttpServletRequest request) throws IOException {

        Booked booked = getBookedRoms(reservationNumber);
        if(booked != null && (
                booked.getBookedStartDate().getDayOfYear() > LocalDate.now().getDayOfYear() ||
                        booked.getBookedEndDate().getDayOfYear() < LocalDate.now().getDayOfYear()
        )){
            model.addAttribute("error", "The check in date is not within your booked dates");
            model.addAttribute("bookedInfo", booked);

            if(request.getServletPath().equals("/checkin")){
                response.sendRedirect("/reservations?check=The check in date is not within your booked dates");
                return null;
            }
            return "reservation";
        }

        Room room = roomrepository.findByRoomNumber(booked.getRoom().getRoomNumber());

        if(room.getStatus().equals("occupied")){
            model.addAttribute("error", "The room is still occupied");
            model.addAttribute("bookedInfo", booked);

            if(request.getServletPath().equals("/checkin")){
                response.sendRedirect("/reservations?check=The room is still occupied");
                return null;
            }

            return "reservation";
        }

        booked.setCheckIn("check_in");
        booked.setCheckInDate(LocalDate.now());
        room.setStatus("occupied");
        booked.setRoom(room);
        bookedRepository.save(booked);

        model.addAttribute("bookedInfo", booked);
        return "reservation";
    }

    @GetMapping("/reservations")
    public String getReservations(Model model,
                                  @RequestParam("check_in") Optional<String> string,
                                  @RequestParam("check") Optional<String> msg
    ){
        List<Booked> bookedList = hotelService.getAllBookedRoom();
        String paramVariable = string.orElse("");
        String errorMsg = msg.orElse("");
        model.addAttribute("error",paramVariable);
        model.addAttribute("error",errorMsg);
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

    @GetMapping("/checkout")
    public void checkout(
            @RequestParam("reservationNumber") String reservationNumber,
            HttpServletResponse response, Model model) throws IOException {

        Booked booked = getBookedRoms(reservationNumber);

        if(booked.getCheckIn() == null) {
            response.sendRedirect("/reservations?check_in=Status: Room not checked in!");
            return;
        }


        booked.setCheckOutDate(LocalDate.now());

        Room room = roomrepository.findByRoomNumber(booked.getRoom().getRoomNumber());
        room.setStatus("available");
        booked.setRoom(room);
        bookedRepository.save(booked);

        model.addAttribute("bookedInfo", booked);

        response.sendRedirect("/reservations");
    }



    @GetMapping("/calendar")
    public String roomsCalender(@RequestParam("room_no") String roomNumber, Model model,
                                HttpServletResponse response, HttpServletRequest request) throws IOException {
        List<RoomCalenderModel> roomCalenderModelList = hotelService.getRoomBookedDates(roomNumber);
        HttpSession session = request.getSession();
        if(!roomCalenderModelList.isEmpty()) {
            model.addAttribute("calendar", roomCalenderModelList);
            model.addAttribute("roomNumber", roomCalenderModelList.get(0).getRoomNumber());
            session.setAttribute("calendar_error",null);
            return "room-calendar";
        }

        Room room = roomrepository.findByRoomNumber(roomNumber);
        if(room == null) {
            session.setAttribute("calendar_error","The room number is invalid");
            response.sendRedirect("/rooms");
            return null;
        }

//        model.addAttribute("notBooked", "The room is not booked");
        session.setAttribute("calendar_error","The room is not booked");
        response.sendRedirect("/rooms");
        return null;
    }

    @GetMapping("book_checkin")
    public String getBookCheckinPage(
            @ModelAttribute("book")BookDto bookDto, Model model,
            @RequestParam("room_no") String roomNumber, @RequestParam("error") Optional<String> msg){
        bookDto.setRoomNumber(roomNumber);
        model.addAttribute("book", bookDto);
        String paramVariable = msg.orElse("");
        model.addAttribute("error", paramVariable);
        return "book_checkin_page";
    }

    @PostMapping("book_checkin")
    public void saveBookCheckinPage(
            @RequestParam("room_no") String roomNumber,
            @ModelAttribute("book") BookDto bookDto,
            HttpServletResponse response, HttpServletRequest request
     ) throws IOException {

        bookDto.setRoomNumber(roomNumber);
       String msg = hotelService.saveBookCheckin(bookDto);
       if(msg.contains("successful")) {
           response.sendRedirect("/reservations");
           return;
       }
       response.sendRedirect("/book_checkin?error="+msg+"&room_no="+roomNumber);
    }

    @GetMapping("/check_out")
    public  void checkout(HttpServletResponse response, @RequestParam("room_no") String roomNumber) throws IOException {
        String reservationNumber = bookedRepository.findReservationNumber(roomNumber);
        response.sendRedirect("/checkout?reservationNumber="+reservationNumber);
    }

    @GetMapping("/room-usage")
    public String getRoomUsage(Model model
    ){
        List<Booked> bookedList = hotelService.getRoomUsage();
        model.addAttribute("bookedInfo",bookedList);
        return "room-usage";

    }


    private  Booked getBookedRoms(String reservationNumber){
     return  hotelService.getRoom(reservationNumber);
    }
}
