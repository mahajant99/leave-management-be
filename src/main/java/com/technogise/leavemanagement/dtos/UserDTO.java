package com.technogise.leavemanagement.dtos;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class UserDTO {

    private String firstName;

    private String lastName;

    private String email;

}
