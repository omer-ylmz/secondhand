package com.omer.secondhand.user.controller;

import com.omer.secondhand.user.dto.*;
import com.omer.secondhand.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{mail}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("mail") String mail) {
        return ResponseEntity.ok(userService.getUserById(mail));  //201
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest userRequest){
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @PutMapping("/{mail}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("mail") String mail, @RequestBody UpdateUserRequest updateUserRequest){
        return ResponseEntity.ok(userService.updateUser(mail, updateUserRequest));
    }

//    @PatchMapping("/{id}")  //userı silmek değil deactive etmek için kullanıyoruz
//    public ResponseEntity<Void> deactiveUser(@PathVariable("id") Long id){
//        userService.deactiveUser(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/{id}")  //userı silmek değil deactivate etmek için kullanıyoruz
//    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id){
//        userService.deleteUser(id);
//        return ResponseEntity.ok().build();
//    }
 }
