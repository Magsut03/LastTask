package com.example.lasttask.service.user;


import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.dto.response.item.ItemResponseDto;
import com.example.lasttask.dto.response.MainPageDataResponseDto;
import com.example.lasttask.dto.response.collection.CollectionResponseDto;
import com.example.lasttask.dto.response.user.UserResponseDto;
import com.example.lasttask.exception.BadRequestException;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.entity.collection.CollectionEntity;
import com.example.lasttask.model.entity.item.ItemEntity;
import com.example.lasttask.repository.CollectionRepository;
import com.example.lasttask.repository.ItemRepository;
import com.example.lasttask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CollectionRepository collectionRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;


    public ApiResponse getMainPageData(){
        List<CollectionEntity> collectionEntityList = collectionRepository.findTop();
        List<CollectionResponseDto> collectionResponseDtos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i < collectionEntityList.size()){
                collectionResponseDtos.add(modelMapper.map(collectionEntityList.get(i), CollectionResponseDto.class));
            }
        }
        List<ItemEntity> itemEntityList = itemRepository.findTopByCreateDate();
        List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (i < itemEntityList.size()){
                itemResponseDtos.add(modelMapper.map(itemEntityList.get(i), ItemResponseDto.class));
            }
        }
        return new ApiResponse(1, "success", new MainPageDataResponseDto(collectionResponseDtos, itemResponseDtos));
    }


    public ApiResponse get(String email) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()){
            throw new BadRequestException("User not found with this Email: " + email);
        }
        UserResponseDto userResponseDto = modelMapper.map(optionalUser.get(), UserResponseDto.class);
        return new ApiResponse(1, "success", userResponseDto);
    }
}
