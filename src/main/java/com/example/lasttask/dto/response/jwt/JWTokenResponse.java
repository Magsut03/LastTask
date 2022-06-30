package com.example.lasttask.dto.response.jwt;

import com.example.lasttask.dto.response.user.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JWTokenResponse {

    private int statusCode;
    private String message;
    private String accessToken;
}
