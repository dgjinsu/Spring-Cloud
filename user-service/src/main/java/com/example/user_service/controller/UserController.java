package com.example.user_service.controller;

import com.example.user_service.dto.UserDto;
import com.example.user_service.service.UserService;
import com.example.user_service.vo.RequestUser;
import com.example.user_service.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Value("${greeting.message}")
    private String message;

    @GetMapping("/health_check")
    public String status() {
        return "It's Working in User Service";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return message;
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody RequestUser user) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = modelMapper.map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

}