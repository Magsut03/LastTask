package com.example.lasttask.controller;

import com.example.lasttask.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/get")
    public ResponseEntity<?> fullTextSearch(@RequestParam(name = "text") String text){
        return ResponseEntity.ok(searchService.fullTextSearch(text));
    }

}
