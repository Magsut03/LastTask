package com.example.lasttask.controller;


import com.example.lasttask.dto.request.user.UserLoginDto;
import com.example.lasttask.dto.request.user.UserRegisterDto;
import com.example.lasttask.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDto userRegisterDto){
        return ResponseEntity.ok().body(authService.register(userRegisterDto));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto){
        return ResponseEntity.ok(authService.login(userLoginDto));
    }

}
