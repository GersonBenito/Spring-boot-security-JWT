package com.security.controllers;

import com.security.controllers.dto.CreateUserDTO;
import com.security.entities.RoleEntity;
import com.security.entities.UserEntity;
import com.security.repositories.IUserRepository;
import com.security.util.ERole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    @Autowired
    private IUserRepository userRepository;

    @GetMapping("/hello")
    public ResponseEntity<Map<String,Object>> hello(){
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello world not secured");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/helloSecured")
    public ResponseEntity<Map<String,Object>> helloSecured(){
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello world secured");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/createUser")
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody CreateUserDTO createUserDTO){

        Map<String, Object> response = new HashMap<>();

        Set<RoleEntity> roles = createUserDTO.getRoles()
                .stream().map(role -> RoleEntity.builder()
                        .name(ERole.valueOf(role))
                        .build()
                ).collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .username(createUserDTO.getUsername())
                .password(createUserDTO.getPassword())
                .email(createUserDTO.getEmail())
                .roles(roles)
                .build();

        userRepository.save(userEntity);

        response.put("message", "Usuario creado");
        response.put("Data", userEntity);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        userRepository.deleteById(id);
        response.put("message", "Usuario borrado con id " + id);
        return ResponseEntity.ok(response);
    }
}
