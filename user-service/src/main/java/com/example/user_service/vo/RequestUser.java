package com.example.user_service.vo;

import lombok.Data;

@Data
public class RequestUser {
    private String email;
    private String name;
    private String pwd;
}
