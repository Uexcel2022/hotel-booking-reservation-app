package com.uexcel.hotelbookingapp.controller;

import com.uexcel.hotelbookingapp.dto.BookDto;
import com.uexcel.hotelbookingapp.dto.RoomDto;
import com.uexcel.hotelbookingapp.entity.Room;
import com.uexcel.hotelbookingapp.service.HotelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RoomController {
    private final HotelService hotelService;

    public RoomController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/user")
    public String user(){
        return "userPage";
    }

    @GetMapping("/admin")
    public String admin(){
        return "adminPage";
    }

    @GetMapping("/rooms")
    public String getRoom(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        String calendarError = (String) session.getAttribute("calendar_error");

        List<Room> rooms = hotelService.getAvailableRooms();
        if(rooms.isEmpty()){
            model.addAttribute("rooms","There no room is available!!!");
            return "noRoomAvailable";
        }
        model.addAttribute("rooms",rooms);
        model.addAttribute("user","user");
        model.addAttribute("calendarError", calendarError);
        return "availableRooms";
    }

    @GetMapping("/room-admin")
    public String getRoomAmin(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        String calendarError = (String) session.getAttribute("calendar_error");

        List<Room> rooms = hotelService.getAvailableRooms();
        if(rooms.isEmpty()){
            model.addAttribute("rooms","There no room is available!!!");
            return "noRoomAvailable";
        }
        model.addAttribute("rooms",rooms);
        model.addAttribute("admin","admin");
        model.addAttribute("calendarError", calendarError);
        return "availableRooms";
    }


    @GetMapping("/add-room")
    public String getAddRoomPage(@ModelAttribute("room") RoomDto roomDto){
        return "addRoom";
    }

    @PostMapping("/add-room")
    public String saveRoom(@ModelAttribute("room") RoomDto roomDto, Model model){
        hotelService.savaRoom(roomDto);
        model.addAttribute("success","Room added successfully!!!");
        return "addRoom";
    }

    @GetMapping("/booking")
    public String getBookingPage(@ModelAttribute("book") BookDto bookDto, @RequestParam("room_no") String string){
        bookDto.setRoomNumber(string);
        return "bookingPage";
    }
    @PostMapping("/booking")
    public String saveBooking(@ModelAttribute("book") BookDto bookDto, Model model,@RequestParam("room_no") String string){
        bookDto.setRoomNumber(string);
       String message= hotelService.saveBooking(bookDto, null);
       if(!message.contains("reservation")) {
           model.addAttribute("error", message);
           return "bookingPage";
       }
        model.addAttribute("success", message);
       return "bookingPage";
    }

}
