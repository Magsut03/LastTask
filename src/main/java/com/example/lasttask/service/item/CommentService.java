package com.example.lasttask.service.item;

import com.example.lasttask.dto.request.item.CommentRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.CommentEntity;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.example.lasttask.repository.*;
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

    private ItemEntity checkItemForExist(Long id){
        Optional<ItemEntity> optionalItem = itemRepository.findById(id);
        if (!optionalItem.isPresent()){
            throw new NotFoundException("Item not found with this Id: " + id);
        }
        return optionalItem.get();
    }

    private UserEntity checkUserForExist(Long userId){
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()){
            throw new NotFoundException("User not found with this Id: " + userId);
        }
        return optionalUser.get();
    }

    private CommentEntity checkCommentForExist(Long commentId){
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (!optionalComment.isPresent()){
            throw new NotFoundException("Comment not found with this Id: " + commentId);
        }
        return optionalComment.get();
    }

    public ApiResponse add(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        UserEntity user = checkUserForExist(userId);
        ItemEntity item = checkItemForExist(itemId);
        CommentEntity commentEntity = modelMapper.map(commentRequestDto, CommentEntity.class);
        commentEntity.setUser(user);
        commentEntity.setItem(item);
        commentRepository.save(commentEntity);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse edit(Long userId, Long itemId, Long commentId, CommentRequestDto commentRequestDto) {
        checkUserForExist(userId);
        checkItemForExist(itemId);
        CommentEntity commentEntity = checkCommentForExist(commentId);

        commentEntity.setText(commentRequestDto.getText());
        commentRepository.save(commentEntity);
        return new ApiResponse(1, "success", null);
    }


    public ApiResponse delete(Long userId, Long itemId, Long commentId) {
        checkUserForExist(userId);
        checkItemForExist(itemId);
        checkCommentForExist(commentId);
        commentRepository.deleteById(commentId);
        return new ApiResponse(1, "success", null);
    }

    public ApiResponse get(Long userId, Long itemId) {
        checkUserForExist(userId);
        checkItemForExist(itemId);
        return new ApiResponse(1, "success", commentRepository.findAllByItem_Id(itemId));
    }
}
