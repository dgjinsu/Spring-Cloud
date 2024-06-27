package com.example.second_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SecondServiceService {

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the First service.";
    }
}