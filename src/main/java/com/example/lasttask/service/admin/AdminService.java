package com.example.lasttask.service.admin;

import com.example.lasttask.dto.request.user.ChangeStateRequestDto;
import com.example.lasttask.dto.response.ApiResponse;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.lasttask.model.enums.RoleEnum.ROLE_ADMIN;
import static com.example.lasttask.model.enums.RoleEnum.ROLE_USER;
import static com.example.lasttask.model.enums.StateEnum.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ApiResponse changeState(ChangeStateRequestDto changeStateRequestDto){

        Optional<UserEntity> optionalUser = userRepository.findById(changeStateRequestDto.getUserId());
        if (!optionalUser.isPresent()){
            throw new NotFoundException("user not found with this Id: " + changeStateRequestDto.getUserId());
        }
        UserEntity user = optionalUser.get();
        switch (changeStateRequestDto.getState()){
            case 0: {
                user.setState(ACTIVE);
                break;
            }
            case 1: {
                user.setState(BLOCKED);
                break;
            }
            case 2: {
                user.setState(DELETED);
                break;
            }
        }
        userRepository.save(user);
        return new ApiResponse(1, "Successfully!", null);
    }





    public ApiResponse changeRole(Long userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()){
            throw new NotFoundException("User not fount with this Id: " + userId);
        }
        UserEntity user = optionalUser.get();

        if (user.getRole().equals(ROLE_USER)) user.setRole(ROLE_ADMIN);
        else user.setRole(ROLE_USER);
        userRepository.save(user);
        return new ApiResponse(1, "Successfully!", null);
    }

}
