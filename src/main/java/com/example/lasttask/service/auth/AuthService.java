package com.example.lasttask.service.auth;

import com.example.lasttask.dto.request.user.UserLoginDto;
import com.example.lasttask.dto.request.user.UserRegisterDto;
import com.example.lasttask.dto.response.jwt.JWTokenResponse;
import com.example.lasttask.dto.response.user.UserResponseDto;
import com.example.lasttask.exception.BadRequestException;
import com.example.lasttask.exception.NotFoundException;
import com.example.lasttask.model.entity.UserEntity;
import com.example.lasttask.model.enums.RoleEnum;
import com.example.lasttask.repository.UserRepository;
import com.example.lasttask.security.provider.JWTokenProvider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.lasttask.model.enums.RoleEnum.ROLE_USER;
import static com.example.lasttask.model.enums.StateEnum.ACTIVE;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTokenProvider jwTokenProvider;


    public JWTokenResponse register(UserRegisterDto userRegisterDto){
        if (userRepository.findByEmail(userRegisterDto.getEmail()).isPresent()){
            throw new BadRequestException("User already registered with: " + userRegisterDto.getEmail());
        }
        UserEntity user = modelMapper.map(userRegisterDto, UserEntity.class);
        user.setRole(ROLE_USER);
        user.setState(ACTIVE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
        String token = jwTokenProvider.generateAccessToken(user);
        return new JWTokenResponse(OK.value(), OK.name(), token, userResponseDto);
    }

    public JWTokenResponse login(UserLoginDto userLoginDto){
        Optional<UserEntity> OpUser = userRepository.findByEmail(userLoginDto.getEmail());
        if (!OpUser.isPresent()){
            throw new NotFoundException("user not found with this email: " + userLoginDto.getEmail());
        }
        UserEntity user = OpUser.get();
        if (passwordEncoder.matches(user.getPassword(), userLoginDto.getPassword())){
            throw new BadRequestException("wrong password!");
        }
        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
        String token = jwTokenProvider.generateAccessToken(user);
        return new JWTokenResponse(OK.value(), OK.name(), token, userResponseDto);
    }
}
