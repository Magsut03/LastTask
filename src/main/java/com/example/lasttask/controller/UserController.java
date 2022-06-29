package com.example.lasttask.controller;

import com.example.lasttask.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/get/{email}")
    public ResponseEntity<?> get(
            @PathVariable(name = "email") String email
    ){
        return ResponseEntity.ok(userService.get(email));
    }




    @GetMapping("/get_main_page")
    public ResponseEntity<?> getTopCollections(){
        return ResponseEntity.ok(userService.getMainPageData());
    }


}
