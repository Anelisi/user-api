package br.com.develcode.userapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserCreateDTO {

    @NotBlank
    String name;
    String birthdate;
    String picture;

}
