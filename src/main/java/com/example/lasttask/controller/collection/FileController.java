package com.example.lasttask.controller.collection;

import com.example.lasttask.service.collection.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class FileController {

    private final FileService fileService;

    @PostMapping("/profile/pic")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile multipartFile) {
        return ResponseEntity.ok(fileService.upload(multipartFile));
    }
}
