package com.example.lasttask.controller;

import com.example.lasttask.dto.request.collection.CollectionRequestDto;
import com.example.lasttask.dto.request.collection.ListFieldRequestDto;
import com.example.lasttask.service.collection.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/collection")
public class CollectionController {

    private final CollectionService collectionService;

    @GetMapping("/get_all")
    public ResponseEntity<?> getAllCollections(){
        return ResponseEntity.ok(collectionService.getAll());
    }

    @GetMapping("/get_main_page")
    public ResponseEntity<?> getTopCollections(){
        return ResponseEntity.ok(collectionService.getMainPageData());
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/add/{userId}")
    public ResponseEntity<?> add(
            @PathVariable(name = "userId") Long userId,
            @RequestBody CollectionRequestDto collectionRequestDto){
        return ResponseEntity.ok(collectionService.add(userId, collectionRequestDto));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/edit/{collectionId}")
    public ResponseEntity<?> edit(
            @PathVariable(name = "collectionId") Long collectionId,
            @RequestBody CollectionRequestDto collectionRequestDto){
        return ResponseEntity.ok(collectionService.edit(collectionId, collectionRequestDto));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/delete/{collectionId}")
    public ResponseEntity<?> delete(
            @PathVariable(name = "collectionId") Long collectionId
    ){
        return ResponseEntity.ok(collectionService.delete(collectionId));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/get_fields/{collectionId}")
    public ResponseEntity<?> get(@PathVariable(name = "collectionId") Long collectionId){
        return ResponseEntity.ok(collectionService.getFields(collectionId));
    }

    @PostMapping("add_fields/{collectionId}")
    public ResponseEntity<?> addFields(
            @PathVariable(name = "collectionId") Long collectionId,
            @RequestBody ListFieldRequestDto listFieldRequestDto
            ){
        return ResponseEntity.ok(collectionService.addField(collectionId, listFieldRequestDto));
    }

}
