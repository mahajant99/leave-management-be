package com.technogise.leavemanagement.dtos;

import lombok.Builder;
import lombok.Data;

import com.technogise.leavemanagement.entities.User;

@Data
@Builder
public class UserDTO {

    private String firstName;

    private String lastName;

    private String email;

    public static final UserDTO convertToDto(User user) {
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
}
