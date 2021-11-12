package br.com.develcode.userapi.controller;

import br.com.develcode.userapi.dto.UserCreateDTO;
import br.com.develcode.userapi.dto.UserDTO;
import br.com.develcode.userapi.entity.User;
import br.com.develcode.userapi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Api(tags = "User")
public class UserController {

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Método responsável por criar o usuário")
    public UserDTO create(@Valid @RequestBody UserCreateDTO userCreateDTO){
        return service.create(userCreateDTO);
    }

    @PatchMapping("/upload-picture/{id}")
    @ApiOperation(value = "Método responsável por carregar a foto do o usuário")
    public String uploadPicture(@PathVariable("id") Long id, @RequestParam MultipartFile file) {
        return service.createFileName(id, file);
    }

    @GetMapping
    @ApiOperation(value = "Método responsável por listar todos os usuários")
    public List<UserDTO> findAll() {
        return service.findAllUsers();
    }

    @PatchMapping("/{id}")
    public @ResponseBody void partUpdate(@RequestBody Map<Object, Object> fields, @PathVariable("id") Long id) {
        final var user =  service.findUserById(id);

        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(User.class, (String) k);
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, user, v);
        });

        service.update(user, id);
    }

    @PutMapping("/{id}")
    public UserDTO update(@Valid @RequestBody UserDTO userDTO, @PathVariable Long id) {
        return service.update(userDTO, id);
    }

    @DeleteMapping("/user/{id}")
    @ApiOperation(value = "Método responsável por excluir o usuário por id")
    public ResponseEntity<User> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return noContent().build();
    }

}
