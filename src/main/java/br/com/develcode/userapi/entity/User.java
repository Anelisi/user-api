package br.com.develcode.userapi.entity;

import br.com.develcode.userapi.dto.UserCreateDTO;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static javax.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private LocalDate birthdate;
    private String picture;

    public static User fromDTO(UserCreateDTO dto) {
        User user = new User();

        user.name = dto.getName();
        user.birthdate = LocalDate.parse(dto.getBirthdate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        user.picture = dto.getPicture();

        return user;
    }
}
