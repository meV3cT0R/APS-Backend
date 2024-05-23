package com.vector.auto.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vector.auto.model.Category;
import com.vector.auto.model.User;
import com.vector.auto.model.UserData;
import com.vector.auto.repository.CatRepo;
import com.vector.auto.repository.UserRepo;


@Service
public class AdminService  {
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/images/products";
    
    @Autowired
    private CatRepo catRepo;
    @Autowired
    private UserRepo userRepo;

    public Category saveCategory(Category category,MultipartFile file) throws IOException {
        category.setImage(saveImage(file));
        return catRepo.save(category);
    }

    public List<UserData> getAllUsers() {
        return userRepo.findAll().stream().map(data-> {
            return new UserData(data);
        }).toList();
    }

    public UserData deleteUser(Long id) throws Exception {
        Optional<User>  user= userRepo.findById(id);
        if(user.isEmpty()) throw new Exception("Not Found");
        userRepo.deleteById(id);
        return new UserData(user.get());
    }
    private String saveImage(MultipartFile file) throws IOException {
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        File directory = new File(UPLOAD_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Files.write(fileNameAndPath, file.getBytes());

        return String.format("%s/%s", "/images/products", file.getOriginalFilename());
    }
}
