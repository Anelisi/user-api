package br.com.develcode.userapi.dto;

import br.com.develcode.userapi.entity.User;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserDTO {

    Long id;
    String name;
    LocalDate birthdate;
    String picture;

    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.name = user.getName();
        userDTO.birthdate = user.getBirthdate();

        return userDTO;
    }
}
