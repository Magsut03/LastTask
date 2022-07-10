package com.example.lasttask.controller.item;

import com.example.lasttask.dto.request.item.CommentRequestDto;
import com.example.lasttask.service.item.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/add/{userId}/{itemId}")
    public ResponseEntity<?> add(
            @RequestBody CommentRequestDto commentRequestDto,
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "itemId") Long itemId
            ){
        return ResponseEntity.ok(commentService.add(userId, itemId, commentRequestDto));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/edit/{userId}/{itemId}/{commentId}")
    public ResponseEntity<?> edit(
            @RequestBody CommentRequestDto commentRequestDto,
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "itemId") Long itemId,
            @PathVariable(name = "commentId") Long commentId
            ){
        return ResponseEntity.ok(commentService.edit(userId, itemId, commentId, commentRequestDto));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/delete/{userId}/{itemId}/{commentId}")
    public ResponseEntity<?> delete(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "itemId") Long itemId,
            @PathVariable(name = "commentId") Long commentId
            ){
        return ResponseEntity.ok(commentService.delete(userId, itemId, commentId));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/get/{userId}/{itemId}")
    public ResponseEntity<?> get(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "itemId") Long itemId
            ){
        return ResponseEntity.ok(commentService.get(userId, itemId));
    }




}
