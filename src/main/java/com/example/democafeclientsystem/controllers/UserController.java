package com.example.democafeclientsystem.controllers;

import com.example.democafeclientsystem.dto.UserDTO;
import com.example.democafeclientsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
@AllArgsConstructor
public class UserController {
    private UserService service;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUser(Authentication authentication) {

        return new ResponseEntity<>(service.getUserByAuth(authentication), HttpStatus.OK);
    }

//    ADMIN MANAGING

    @GetMapping("/users/manage")
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        return new ResponseEntity<>(service.manageGetAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/manage/{id}")
    public ResponseEntity<UserDTO> manageGetUser(@PathVariable("id") int id) {

        return new ResponseEntity<>(service.manageGetUser(id), HttpStatus.OK);
    }

    @PutMapping("/users/manage/{id}")
    public ResponseEntity<UserDTO> manageUpdateUser(@PathVariable("id") int id,
                                                    @RequestBody String role) {
        return new ResponseEntity<>(service.manageUpdateUser(id, role), HttpStatus.OK);
    }
}
