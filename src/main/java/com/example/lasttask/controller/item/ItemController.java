package com.example.lasttask.controller.item;

import com.example.lasttask.dto.request.item.ItemRequestDto;
import com.example.lasttask.service.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/item")
public class ItemController{

    private final ItemService itemService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/add/{userId}/{collectionId}")
    public ResponseEntity<?> add(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "collectionId") Long collectionId,
            @RequestBody ItemRequestDto itemRequestDto
            ){
        return ResponseEntity.ok(itemService.add(userId, collectionId, itemRequestDto));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/edit/{userId}/{collectionId}/{itemId}")
    public ResponseEntity<?> edit(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "collectionId") Long collectionId,
            @PathVariable(name = "itemId") Long itemId,
            @RequestBody ItemRequestDto itemRequestDto
    ){
        return ResponseEntity.ok(itemService.edit(userId, collectionId, itemId, itemRequestDto));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/delete/{userId}/{collectionId}/{itemId}")
    public ResponseEntity<?> delete(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "collectionId") Long collectionId,
            @PathVariable(name = "itemId") Long itemId
    ){
        return ResponseEntity.ok(itemService.delete(userId, collectionId, itemId));
    }

    @GetMapping("/get/{collectionId}/{itemId}")
    public ResponseEntity<?> getById(
            @PathVariable(name = "collectionId") Long collectionId,
            @PathVariable(name = "itemId") Long itemId
    ) {
       return ResponseEntity.ok(itemService.getById(collectionId, itemId));
    }

    @GetMapping("/get_all/{collectionId}")
    public ResponseEntity<?> getAll(
            @PathVariable(name = "collectionId") Long collectionId
    ) {
       return ResponseEntity.ok(itemService.getAll(collectionId));
    }
}
