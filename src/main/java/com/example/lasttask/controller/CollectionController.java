package com.example.lasttask.controller;

import com.example.lasttask.dto.request.collection.CollectionRequestDto;
import com.example.lasttask.service.collection.CollectionService;
import com.example.lasttask.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/collection")
public class CollectionController {

    private final CollectionService collectionService;

    // ADD
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/add/{userId}")
    public ResponseEntity<?> add(
            @PathVariable(name = "userId") Long userId,
            @RequestBody CollectionRequestDto collectionRequestDto){
        return ResponseEntity.ok(collectionService.add(userId, collectionRequestDto));
    }

    // EDIT
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/edit/{userId}/{collectionId}")
    public ResponseEntity<?> edit(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "collectionId") Long collectionId,
            @RequestBody CollectionRequestDto collectionRequestDto){
        return ResponseEntity.ok(collectionService.edit(userId, collectionId, collectionRequestDto));
    }

    // DELETE
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/delete/{userId}/{collectionId}")
    public ResponseEntity<?> delete(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "collectionId") Long collectionId
    ){
        return ResponseEntity.ok(collectionService.delete(userId, collectionId));
    }

    // GET ALL
    @GetMapping("/get_all")
    public ResponseEntity<?> getAllCollections(){
        return ResponseEntity.ok(collectionService.getAll());
    }




}
