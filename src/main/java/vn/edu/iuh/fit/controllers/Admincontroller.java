package vn.edu.iuh.fit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class Admincontroller {

    @GetMapping
    public String admin(){
        return "LOGIN ROLE ADMIN";
    }
}
