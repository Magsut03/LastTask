package com.example.lasttask.controller.item;

import com.example.lasttask.dto.request.item.TagRequestDto;
import com.example.lasttask.service.item.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/tag")
public class TagController {

    private final TagService tagService;


    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody TagRequestDto tagRequestDto){
        return ResponseEntity.ok(tagService.add(tagRequestDto));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/edit/{tagId}")
    public ResponseEntity<?> edit(
            @PathVariable(name = "tagId") Long tagId,
            @RequestBody TagRequestDto tagRequestDto
    ){
        return ResponseEntity.ok(tagService.edit(tagId, tagRequestDto));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long tagId){
        return ResponseEntity.ok(tagService.delete(tagId));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/get_all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(tagService.getAll());
    }
}
