package dbc.vemser.refoundapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dbc.vemser.refoundapi.dataTransfer.user.UserCreateDTO;
import dbc.vemser.refoundapi.dataTransfer.user.UserDTO;
import dbc.vemser.refoundapi.dataTransfer.user.UserUpdateDTO;
import dbc.vemser.refoundapi.entity.RoleEntity;
import dbc.vemser.refoundapi.entity.UserEntity;
import dbc.vemser.refoundapi.exception.BusinessRuleException;
import dbc.vemser.refoundapi.repository.RoleRepository;
import dbc.vemser.refoundapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    private final RoleRepository roleRepository;


    public UserDTO save(UserCreateDTO userCreate, String role) throws Exception {
        log.info("Chamada de método:: SAVE USER!");
        Optional<UserEntity> user = userRepository.findByEmail(userCreate.getEmail());
        if (user.isPresent()) {
            throw new BusinessRuleException("Email already exists!");
        }

        UserEntity userEntity = objectMapper.convertValue(userCreate, UserEntity.class);
        setPhoto(userEntity, userCreate);

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity roleEntity = roleRepository.findById(Integer.parseInt(role))
                .orElseThrow(() -> new BusinessRuleException("Role not found!"));
        roles.add(roleEntity);
        userEntity.setRoleEntities(roles);

        userEntity.setPassword(new BCryptPasswordEncoder().encode(userCreate.getPassword()));

        UserEntity userSaved = userRepository.save(userEntity);
        return buildUserDTO(userSaved);
    }

    //ADMIN
//    public List<UserDTO> list() {
//        log.info("Chamada de método:: LIST USER!");
//        return userRepository.findAll().stream()
//                .map(this::buildUserDTO)
//                .collect(Collectors.toList());
//    }
    public Page<UserDTO> listOrderById(Integer requestPage, Integer sizePage) {
        Pageable pageable = PageRequest.of(requestPage, sizePage, Sort.by("idUser").ascending());
        return userRepository.findAll(pageable)
                .map(this::buildUserDTO);
    }

    public UserDTO getById (Integer id) throws BusinessRuleException {
        UserEntity userFound = userRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("User not found!"));
        return buildUserDTO(userFound);
    }


    public UserDTO update(Integer id, UserUpdateDTO userAtt) throws Exception {
        log.info("Chamada de método:: UPDATE USER!");
        UserEntity userFound = userRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("User not found!"));

        if (userAtt.getImage() != null && !userAtt.getImage().isEmpty()) {
            UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                    .image(userAtt.getImage())
                    .build();
            setPhoto(userFound, userCreateDTO);
        }
        if (userAtt.getPassword() != null && !userAtt.getPassword().isEmpty() && !userAtt.getPassword().isBlank()) {
            userFound.setPassword(new BCryptPasswordEncoder().encode(userAtt.getPassword()));
        }
        UserEntity userEntityAtt = userRepository.save(userFound);
        return buildUserDTO(userEntityAtt);
    }

    public UserDTO updateAdmin(String id, UserCreateDTO userAtt, String role) throws BusinessRuleException {
        log.info("Chamada de método:: UPDATE USER!");
        UserEntity userFound = userRepository.findById(Integer.parseInt(id))
                .orElseThrow(() -> new BusinessRuleException("User not found!"));

        if (userAtt.getImage() != null && !userAtt.getImage().isEmpty()) {
            UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                    .image(userAtt.getImage())
                    .build();
            setPhoto(userFound, userCreateDTO);
        }
        if (userAtt.getPassword() != null && !userAtt.getPassword().isEmpty() && !userAtt.getPassword().isBlank()) {
            userFound.setPassword(new BCryptPasswordEncoder().encode(userAtt.getPassword()));
        }
        userFound.setName(userAtt.getName());
        userFound.setEmail(userAtt.getEmail());

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity roleEntity = roleRepository.findById(Integer.parseInt(role))
                .orElseThrow(() -> new BusinessRuleException("Role not found!"));
        roles.add(roleEntity);
        userFound.setRoleEntities(roles);
        UserEntity userEntityAtt = userRepository.save(userFound);
        return buildUserDTO(userEntityAtt);
    }


    public UserDTO delete(Integer id) throws Exception {
        log.info("Chamada de método:: DELETE USER!");
        UserEntity userFound = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        userRepository.delete(userFound);
        return objectMapper.convertValue(userFound, UserDTO.class);
    }

    public List<UserDTO> findByNameContainingIgnoreCase(String name) throws Exception {
        return userRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::buildUserDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Page<UserDTO> orderByName(Integer requestPage, Integer sizePage) {
        Pageable pageable = PageRequest.of(requestPage, sizePage, Sort.by("name").ascending());
        return userRepository.findAll(pageable)
                .map(this::buildUserDTO);
    }

    private UserDTO buildUserDTO(UserEntity user) {
        UserDTO userDTO = UserDTO.builder()
                .idUser(user.getIdUser())
                .name(user.getName())
                .email(user.getEmail())
                .roleEntities(user.getRoleEntities())
                .build();

        if (user.getImage() != null) {
            userDTO.setImage(Base64.getEncoder().encodeToString(user.getImage()));
        }
        return userDTO;
    }

    private void setPhoto(UserEntity userEntity, UserCreateDTO userCreate) {
        try {
            MultipartFile coverPhoto = userCreate.getImage();
            if (coverPhoto != null) {
                userEntity.setImage(coverPhoto.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
