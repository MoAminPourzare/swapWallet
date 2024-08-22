package com.example.rest_service.Controllers;

import com.example.rest_service.Entities.User;
import com.example.rest_service.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> signup(@RequestBody Map<String, String> input) {
        String username = input.get("username");
        String password = input.get("password");
        User newUser = new User(username, password);
        userService.addUser(newUser);
        return new ResponseEntity<>("signup successfully!", HttpStatus.OK);
    }
}