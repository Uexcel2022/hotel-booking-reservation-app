package com.uexcel.hotelbookingapp.controller;

import com.uexcel.hotelbookingapp.dto.RegDto;
import com.uexcel.hotelbookingapp.service.HotelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class AuthenticationController {

   private final HotelService hotelService;

    public AuthenticationController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/register")
    public  String getRegistrationPage(@ModelAttribute("reg") RegDto regDto){
        return  "registration";
    }

    @PostMapping("/register")
    public  void getUser(
            @ModelAttribute("reg") RegDto regDto, HttpServletResponse response, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("regForm", regDto);
        response.sendRedirect("/save-user");
    }

    @GetMapping("/save-user")
    public  String saveUser( Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        RegDto regDto = (RegDto) session.getAttribute("regForm");
        hotelService.saveUSer(regDto);
        model.addAttribute("success", "Successful");
        return  "registration";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

}
