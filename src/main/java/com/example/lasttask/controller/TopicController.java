package com.example.lasttask.controller;

import com.example.lasttask.dto.request.collection.TopicRequestDto;
import com.example.lasttask.service.collection.TopicService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/topic")
public class TopicController {

    private final TopicService topicService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody TopicRequestDto topicRequestDto){
        return ResponseEntity.ok(topicService.add(topicRequestDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/edit/{topicId}")
    public ResponseEntity<?> delete(
            @PathVariable(name = "topicId") Long topicId,
            @RequestBody TopicRequestDto topicRequestDto
    ){
        return ResponseEntity.ok(topicService.edit(topicId, topicRequestDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete/{topicId}")
    public ResponseEntity<?> delete(@PathVariable(name = "topicId") Long topicId){
        return ResponseEntity.ok(topicService.delete(topicId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get_all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(topicService.getAll());
    }
}
