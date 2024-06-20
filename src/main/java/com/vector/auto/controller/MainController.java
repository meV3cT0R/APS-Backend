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
import com.vector.auto.model.Category;
import com.vector.auto.model.LoginForm;

import com.vector.auto.model.Role;
import com.vector.auto.model.TokenLogin;
import com.vector.auto.model.User;
import com.vector.auto.model.UserData;
import com.vector.auto.model.LoginResponse;
import com.vector.auto.repository.CatRepo;
import com.vector.auto.repository.PartsRepo;
import com.vector.auto.repository.UserRepo;
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
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CatRepo catRepo;
    
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@RequestBody User userInfo) {
        Optional<User> user = userRepo.findByUsername(userInfo.getUsername());
        if(user.isPresent()) return new ResponseEntity<>("username already exists",HttpStatus.CONFLICT);
        
        user = userRepo.findByEmail(userInfo.getEmail());
        if(user.isPresent()) return new ResponseEntity<>("email already exists",HttpStatus.CONFLICT);


        userInfo.setRole(Role.USER);
        return new ResponseEntity<>(service.addUser(userInfo),HttpStatus.OK);
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

    @GetMapping("/getLatestProducts")
    public ResponseEntity<List<Autopart>> getLatestProducts() {
        List<Autopart> opart = partsRepo.getLatestAutoparts();


        return new ResponseEntity<>(opart,HttpStatus.OK);
    }


    @GetMapping("/getCategories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> cats = catRepo.findAll();
        return new ResponseEntity<>(cats,HttpStatus.OK);
    }
    @GetMapping("/getCategories/{id}")
    public ResponseEntity<Category> getAllCategories(@PathVariable("id") Long id) {
        Optional<Category> cat = catRepo.findById(id);

        if(cat.isEmpty()) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(cat.get(),HttpStatus.OK);
    }

    @GetMapping("/getBrands")
    public ResponseEntity<Set<String>> getAllBrands() {
        Set<String> set = new HashSet<>(partsRepo.getAllBrands());
        return new ResponseEntity<>(set,HttpStatus.OK);
    }


    @PostMapping("/loginWithToken")
    public ResponseEntity<User> loginWithToken(@RequestBody TokenLogin tokenLogin) {
        boolean valid = jwtService.validateToken(tokenLogin.getToken());
        String username = jwtService.extractUsername(tokenLogin.getToken());
        
        Optional<User> user = userRepo.findByUsername(username);

        if(valid && user.isPresent())
            return new ResponseEntity<>(user.get(),HttpStatus.OK);   
        return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
    }


    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }




    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateAndGetToken(@RequestBody LoginForm authRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authRequest.getUsername());
            Optional<User> user = userRepo.findByUsername(authRequest.getUsername());
            if(user.isEmpty()) throw new Exception("User with username doesn't exists");
            return new ResponseEntity<>(new LoginResponse(new UserData(user.get()),token,""),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new LoginResponse("Invalid Username/Password"),HttpStatus.FORBIDDEN);
        }
    }

}
