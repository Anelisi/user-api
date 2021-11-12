package br.com.develcode.userapi.service;

import br.com.develcode.userapi.dto.UserCreateDTO;
import br.com.develcode.userapi.dto.UserDTO;
import br.com.develcode.userapi.entity.User;
import br.com.develcode.userapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private UserRepository repository;

    public UserDTO create(final UserCreateDTO userCreateDTO) {

        User user = User.fromDTO(userCreateDTO);
        final var userSaved = repository.save(user);
        return UserDTO.fromUser(userSaved);
    }

    public void savePicture(MultipartFile file, String fileName) throws IOException {
        var archive = Paths.get(File.separator + "picture" + File.separator + fileName);
        byte[] bytes = file.getBytes();
        Files.write(archive, bytes);
    }

    public String createFileName(Long id, MultipartFile file) {
        var user = repository.findById(id).orElse(null);
        String originalName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalName);
        String fileName;
        assert user != null;
        fileName = validFileNameFixedOrUUIDRandom(user, extension);
        try {
            savePicture(file, fileName);
            repository.save(user);
        } catch (IOException e) {
            log.error("Error saving picture", e);
        }
        return fileName;
    }

    private String validFileNameFixedOrUUIDRandom(User user, String extension) {
        String fileName;
        if (StringUtils.hasText(user.getPicture())) {
            fileName = user.getPicture();
        } else {
            fileName = UUID.randomUUID() + "." + extension;
            user.setPicture(fileName);
        }
        return fileName;
    }

    public List<UserDTO> findAllUsers() {
        return repository.findAll().stream().map(UserDTO::fromUser).collect(Collectors.toList());
    }

    public UserDTO findUserById(Long id) {
        return repository.findById(id).map(UserDTO::fromUser).orElseThrow();
    }

    public UserDTO update(UserDTO userDTO, Long id) {
        final var user = repository.findById(id).orElse(null);
        assert user != null;
        user.setName(userDTO.getName());
        user.setBirthdate(userDTO.getBirthdate());
        repository.save(user);
        return findUserById(user.getId());
    }

    public void delete(Long id) {
        final var user = repository.findById(id).orElse(null);
        assert user != null;
        repository.delete(user);
    }
}
