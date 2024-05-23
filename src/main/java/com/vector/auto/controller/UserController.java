package com.vector.auto.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vector.auto.model.CartsBody;
import com.vector.auto.model.Order;
import com.vector.auto.services.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
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
}
