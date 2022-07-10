package com.example.lasttask.controller.collection;

import com.example.lasttask.dto.request.collection.FieldRequestDto;
import com.example.lasttask.service.collection.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/field")
public class FieldController {

    private final FieldService fieldService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/add/{userId}/{collectionId}")
    public ResponseEntity<?> add(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "collectionId") Long collectionId,
            @RequestBody FieldRequestDto fieldRequestDto
    ){
        return ResponseEntity.ok(fieldService.add(userId, collectionId, fieldRequestDto));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/delete/{userId}/{collectionId}/{fieldId}")
    public ResponseEntity<?> delete(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "collectionId") Long collectionId,
            @PathVariable(name = "fieldId") Long fieldId
    ){
        return ResponseEntity.ok(fieldService.delete(userId, collectionId, fieldId));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/get_all/{userId}/{collectionId}")
    public ResponseEntity<?> get(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "collectionId") Long collectionId
    ){
        return ResponseEntity.ok(fieldService.getAll(userId, collectionId));
    }


}
