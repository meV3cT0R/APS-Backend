package com.vector.auto.controller;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vector.auto.model.Autopart;
import com.vector.auto.model.AutopartForm;
import com.vector.auto.model.LoginForm;

import com.vector.auto.model.Role;
import com.vector.auto.model.User;
import com.vector.auto.repository.PartsRepo;
import com.vector.auto.services.JwtService;
import com.vector.auto.services.UserInfoService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PartsRepo partsRepo;
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/register")
    public String addNewUser(@RequestBody User userInfo) {
        userInfo.setRole(Role.USER);
        return service.addUser(userInfo);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<Autopart>> getAllProducts() {
        return new ResponseEntity<>(partsRepo.findAll(),HttpStatus.OK);
    }

    @GetMapping("/getAllProducts/{id}")
    public ResponseEntity<Autopart> productById(@PathVariable Long id) {
        Optional<Autopart> opart = partsRepo.findById(id);

        if(opart.isPresent())return new ResponseEntity<>(opart.get(),HttpStatus.OK);

        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }


    @GetMapping("/getCategories")
    public ResponseEntity<Set<String>> getAllCategories() {
        Set<String> set = new HashSet<>(partsRepo.getAllCategory());
        return new ResponseEntity<>(set,HttpStatus.OK);
    }

    @GetMapping("/getBrand")
    public ResponseEntity<Set<String>> getAllBrand() {
        Set<String> set = new HashSet<>(partsRepo.getAllBrands());
        return new ResponseEntity<>(set,HttpStatus.OK);
    }
    
    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }


    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody LoginForm authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            return "Invalid Username/Password";
        }
    }

}
