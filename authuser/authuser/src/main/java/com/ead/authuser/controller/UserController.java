package com.ead.authuser.controller;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.dto.UserView;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable(value = "userId")UUID userId){
        Optional<UserModel> optionalUserModel = userService.findById(userId);
        if(optionalUserModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(optionalUserModel.get());
        }

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId")UUID userId){
        Optional<UserModel> optionalUserModel = userService.findById(userId);
        if(optionalUserModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }else{
            userService.delete(optionalUserModel.get());
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId")UUID userId,
                                             @RequestBody @Validated(UserView.UserPut.class)
                                             @JsonView(UserView.UserPut.class) UserDto userDto){
        Optional<UserModel> optionalUserModel = userService.findById(userId);
        if(optionalUserModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }else{
            final var userModel = optionalUserModel.get();
            userModel.setFullName(userDto.getFullName());
            userModel.setPhoneNumber(userDto.getPhoneNumber());
            userModel.setCpf(userDto.getCpf());
            userModel.setLastUpdate(LocalDateTime.now(ZoneId.of("UTC")));

            userService.save(userModel);

            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId")UUID userId,
                                                 @RequestBody @Validated(UserView.PasswordPut.class)
                                                 @JsonView(UserView.PasswordPut.class) UserDto userDto){
        Optional<UserModel> optionalUserModel = userService.findById(userId);
        if(optionalUserModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } if(!userDto.getOldPassword().equals(optionalUserModel.get().getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Old password not match");
        }if(userDto.getPassword().length()<8){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Password must have be at least 8 characteres");
        }
        else{
            final var userModel = optionalUserModel.get();
            userModel.setPassword(userDto.getPassword());
            userModel.setLastUpdate(LocalDateTime.now(ZoneId.of("UTC")));

            userService.save(userModel);

            return ResponseEntity.status(HttpStatus.OK).body("Password updated");
        }
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "userId")UUID userId,
                                              @RequestBody @Validated(UserView.ImagePut.class)
                                              @JsonView(UserView.ImagePut.class) UserDto userDto){
        Optional<UserModel> optionalUserModel = userService.findById(userId);
        if(optionalUserModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }else{
            final var userModel = optionalUserModel.get();
            userModel.setImageUrl(userDto.getImageUrl());
            userModel.setLastUpdate(LocalDateTime.now(ZoneId.of("UTC")));

            userService.save(userModel);

            return ResponseEntity.status(HttpStatus.OK).body(userModel);
        }
    }

}
