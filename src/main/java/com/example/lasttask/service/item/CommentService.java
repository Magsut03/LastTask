package com.example.lasttask.service.item;

import com.example.lasttask.dto.request.item.CommentRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.CommentEntity;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.example.lasttask.repository.*;
import com.example.lasttask.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final CheckService checkService;




    public ApiResponse add(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        UserEntity user = checkService.checkUserForExist(userId);
        ItemEntity item = checkService.checkItemForExist(itemId);
        CommentEntity commentEntity = modelMapper.map(commentRequestDto, CommentEntity.class);
        commentEntity.setUser(user);
        commentEntity.setItem(item);
        commentRepository.save(commentEntity);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse edit(Long userId, Long itemId, Long commentId, CommentRequestDto commentRequestDto) {
        checkService.checkUserForExist(userId);
        checkService.checkItemForExist(itemId);
        CommentEntity commentEntity = checkService.checkCommentForExist(commentId);

        commentEntity.setText(commentRequestDto.getText());
        commentRepository.save(commentEntity);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse delete(Long userId, Long itemId, Long commentId) {
        checkService.checkUserForExist(userId);
        checkService.checkItemForExist(itemId);
        checkService.checkCommentForExist(commentId);
        commentRepository.deleteById(commentId);
        return new ApiResponse(1, "success", null);
    }

    public ApiResponse get(Long userId, Long itemId) {
        checkService.checkUserForExist(userId);
        checkService.checkItemForExist(itemId);
        return new ApiResponse(1, "success", commentRepository.findAllByItem_Id(itemId));
    }
}
