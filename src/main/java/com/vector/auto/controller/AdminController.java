package com.vector.auto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vector.auto.model.Autopart;
import com.vector.auto.model.AutopartForm;
import com.vector.auto.repository.PartsRepo;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private PartsRepo partsRepo;
    
    
    @PostMapping("/saveProduct")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Autopart saveProduct(@RequestBody AutopartForm prod) {
        return partsRepo.save(new Autopart(prod));
    }
}
