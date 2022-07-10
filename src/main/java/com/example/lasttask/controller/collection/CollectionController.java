package com.example.lasttask.controller.collection;

import com.example.lasttask.dto.request.collection.CollectionRequestDto;
import com.example.lasttask.dto.response.collection.CollectionResponseDto;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.service.collection.CollectionService;
import com.example.lasttask.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.io.ICsvWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.supercsv.prefs.CsvPreference.STANDARD_PREFERENCE;

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
            @RequestBody CollectionRequestDto collectionRequestDto
    ){

        return ResponseEntity.ok(collectionService.add(userId, collectionRequestDto));
    }

    // EDIT
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/edit/{userId}/{collectionId}")
    public ResponseEntity<?> edit(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "collectionId") Long collectionId,
            @RequestBody CollectionRequestDto collectionRequestDto
    ){
        return ResponseEntity.ok(collectionService.edit(userId, collectionId, collectionRequestDto));
    }

    // DELETE
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/delete/{userId}/{collectionId}")
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

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("get/{userId}")
    public ResponseEntity<?> getUserCollections(
            @PathVariable(name = "userId") Long userId
    ){
        return ResponseEntity.ok(collectionService.getUserCollections(userId));
    }

    @GetMapping("/get_by_topic/{topicId}")
    public ResponseEntity<?> getByTopic(
            @PathVariable(name = "topicId") Long id
    ){
        return ResponseEntity.ok(collectionService.getByTopic(id));
    }


    @GetMapping("/export/{collectionId}")
    public void exportToCSV(HttpServletResponse response, @PathVariable(name = "collectionId") Long collectionId) {
        collectionService.exportToCSV(response, collectionId);
    }





}
