package com.ead.authuser.controller;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.dto.UserView;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserView.RegistrationPost.class)
                                                   @JsonView(UserView.RegistrationPost.class)
                                                           UserDto userDto){

        log.debug("POST registerUser - userDTO receiver: {}", userDto.toString());
        if(userService.existsByUserName(userDto.getUsername())){
            log.warn("POST registerUser - This username {} is not available", userDto.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error:This username is not available");
        }

        if(userService.existsByEmail(userDto.getEmail())){
            log.warn("POST registerUser - This email {} is not available", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email registered already");
        }

        final var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);

        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdate(LocalDateTime.now(ZoneId.of("UTC")));

        userService.save(userModel);

        log.debug("POST registerUser - userDTO registered: {}", userModel.toString());
        log.info("User saved succesfully - userId: {}", userModel.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
}
