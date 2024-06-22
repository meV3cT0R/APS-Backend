package com.vector.auto.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vector.auto.model.CartsBody;
import com.vector.auto.model.Order;
import com.vector.auto.model.User;
import com.vector.auto.model.UserData;
import com.vector.auto.repository.UserRepo;
import com.vector.auto.services.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderService orderService;
    
    @GetMapping("/userProfile")
    @PreAuthorize("hasAuthority('USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }
    
    @PostMapping("/checkOutItems")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Order> addItemsToCart(@RequestBody CartsBody cart,HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        return new ResponseEntity<>(orderService.saveOrder(cart,token),HttpStatus.OK);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<UserData> updateUser(@RequestParam("id") Long id,@RequestParam("name") String name,@RequestParam("address") String address,@RequestParam("phone") String phone) {
        Optional<User> user = userRepo.findById(id);

        if(user.isPresent()) {
            user.get().setName(name);
            user.get().setAddress(address); 
            user.get().setPhoneNumber(phone);
            return new ResponseEntity<UserData>(new UserData(user.get()),HttpStatus.OK);
        }

        return new ResponseEntity<>(new UserData(),HttpStatus.NOT_FOUND);
    }
}
