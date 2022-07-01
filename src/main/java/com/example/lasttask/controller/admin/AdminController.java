package com.example.lasttask.controller.admin;

import com.example.lasttask.dto.request.user.ChangeStateRequestDto;
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
    public ResponseEntity<?> changeState(@RequestBody ChangeStateRequestDto changeStateRequestDto){
        return ResponseEntity.ok(adminService.changeState(changeStateRequestDto));
    }




}
