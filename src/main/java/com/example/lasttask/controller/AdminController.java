package com.example.lasttask.controller;

import com.example.lasttask.dto.request.user.ListChangeStateRequestDto;
import com.example.lasttask.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/change_role/{userId}")
    public ResponseEntity<?> changeRole(@PathVariable(name = "userId") Long userId){
        return ResponseEntity.ok(adminService.changeRole(userId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/change_state")
    public ResponseEntity<?> changeState(@RequestBody ListChangeStateRequestDto changeStateRequestDtos){
        return ResponseEntity.ok(adminService.changeState(changeStateRequestDtos));
    }

    @GetMapping("/get_all_users")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(adminService.getAllUsers());
    }



}
