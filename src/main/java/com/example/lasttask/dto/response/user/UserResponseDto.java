package com.example.lasttask.dto.response.user;

import com.example.lasttask.model.enums.RoleEnum;
import com.example.lasttask.model.enums.StateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private RoleEnum role;
    private StateEnum state;

}
