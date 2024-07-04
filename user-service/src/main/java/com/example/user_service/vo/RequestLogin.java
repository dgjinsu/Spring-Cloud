package com.example.user_service.vo;


import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class RequestLogin {
    private String email;
    private String password;
}