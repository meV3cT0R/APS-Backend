package com.vector.auto.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vector.auto.model.Autopart;
import com.vector.auto.model.AutopartForm;
import com.vector.auto.model.AutopartName;
import com.vector.auto.model.Category;
import com.vector.auto.model.Order;
import com.vector.auto.model.Role;
import com.vector.auto.model.User;
import com.vector.auto.model.UserData;
import com.vector.auto.repository.AutopartNameRepo;
import com.vector.auto.repository.CatRepo;
import com.vector.auto.repository.PartsRepo;
import com.vector.auto.repository.UserRepo;
import com.vector.auto.services.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private PartsRepo partsRepo;

    @Autowired
    private CatRepo catRepo;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AutopartNameRepo apNameRepo;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/images/products";

    @DeleteMapping("deleteProduct/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Autopart> deleteProduct(@PathVariable("id") Long id) {
        Optional<Autopart> oAutoPart = partsRepo.findById(id);
        if (oAutoPart.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        for (String url : oAutoPart.get().getImages()) {
            File file = new File(System.getProperty("user.dir") + "/src/main/resources" + url);
            if (file.exists())
                file.delete();
        }
        partsRepo.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("updateProduct/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Autopart updateProduct(@ModelAttribute AutopartForm prod, @RequestParam("specs") String specs,
            @RequestParam(name = "imageList", required = false) MultipartFile[] files, @PathVariable("id") Long id)
            throws Exception {
        System.out.println("Multipart files :" + files);
        System.out.println("Brand New : " + prod.getBrandNew());

        Optional<Autopart> oAutoPart = partsRepo.findById(id);
        if (oAutoPart.isEmpty())
            return new Autopart();
        Autopart autopart = oAutoPart.get();
        if(autopart.getAName()==null) 
                autopart.setAName(new AutopartName());
        if (prod.getName() != null && !prod.getName().trim().isEmpty())
            autopart.getAName().setName(prod.getName());
        if (prod.getCategory() != null) {
            Optional<Category> category = catRepo.findById(prod.getCategory());
            if (category.isEmpty())
                throw new Exception("No category with id : " + prod.getCategory());
            autopart.setCategory(category.get());
        }

        if (prod.getPrice() != null)
            autopart.getAName().setPrice(prod.getPrice());
        
        if (prod.getBrandNew() != null)
            autopart.setBrandNew(prod.getBrandNew());


        if (specs != null && !specs.trim().isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println("Specs : " + specs);
            try {
                Map<String, String> specsMap = objectMapper.readValue(specs, new TypeReference<Map<String, String>>() {
                });
                autopart.setSpecs(specsMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<String> images = new LinkedList<>();
        File directory = new File(UPLOAD_DIRECTORY);

        if (!directory.exists())
            directory.mkdirs();

        if (files != null) {
            for (String image : autopart.getImages()) {

                File file = new File(System.getProperty("user.dir") + "/src/main/resources" + image);
                if (file.exists())
                    file.delete();
            }
            for (MultipartFile file : files) {
                images.add(String.format("%s/%s", "/images/products", file.getOriginalFilename()));

                Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());

                if (!fileNameAndPath.toFile().exists())
                    Files.write(fileNameAndPath, file.getBytes());

            }
            autopart.setImages(images);
        }
        
        return partsRepo.save(autopart);
    }

    @PostMapping("/saveProduct")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Autopart saveProduct(@ModelAttribute AutopartForm prod,
            @RequestParam(name = "specs", required = false) String specs,
            @RequestParam(name = "imageList", required = false) MultipartFile[] files) throws Exception {
        Autopart part = new Autopart(prod);
        AutopartName apName = new AutopartName(prod.getName(),prod.getPrice());
        apNameRepo.save(apName);
        part.setAName(apName);
        System.out.println("Brand New : " + part.getBrandNew());
        Optional<Category> category = catRepo.findById(prod.getCategory());
        if (category.isEmpty())
            throw new Exception("No category with id : " + prod.getCategory());
        part.setCategory(category.get());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, String> specsMap = objectMapper.readValue(specs, new TypeReference<Map<String, String>>() {
            });
            System.out.println(specsMap);
            part.setSpecs(specsMap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> images = new LinkedList<>();

        for (MultipartFile file : files) {
            images.add(String.format("%s/%s", "/images/products", file.getOriginalFilename()));
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
            File directory = new File(UPLOAD_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            Files.write(fileNameAndPath, file.getBytes());
        }
        part.setImages(images);
        return partsRepo.save(part);
    }

    @PostMapping("/saveCategory")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Category> saveCategory(@RequestParam(name = "id", required = false) Long id,
            @RequestParam("name") String name, @RequestParam(name = "file", required = false) MultipartFile file)
            throws IOException {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return new ResponseEntity<>(adminService.saveCategory(category, file), HttpStatus.OK);
    }

    @DeleteMapping("/deleteCategory/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Category> deleteCategory(@PathVariable("id") Long id) throws Exception {
        System.out.println("Function Being Called?");
        Optional<Category> cat = catRepo.findById(id);
        if (cat.isEmpty())
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        
        String image = cat.get().getImage();
        try {
            catRepo.deleteById(id);
        }catch(Exception ex) {
            new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
            throw new Exception(ex);
        }
        
        File file = new File(System.getProperty("user.dir") + "/src/main/resources" + image);
        if (file.exists())
            file.delete();
        System.out.println("Succesfully deleted?");
        return new ResponseEntity<>(new Category(), HttpStatus.OK);
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserData>> getAllUsers() {
        return new ResponseEntity<>(userRepo.findAll().stream().map(dat -> new UserData(dat)).toList(), HttpStatus.OK);
    }


    @DeleteMapping("/deleteUser/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserData> deleteUser(@PathVariable("id") Long id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()){ 
            if(user.get().getRole()==Role.ADMIN)
                return new ResponseEntity<>(new UserData(),HttpStatus.BAD_REQUEST);
            userRepo.deleteById(id);
            return new ResponseEntity<>(new UserData(user.get()),HttpStatus.OK);
        }
        
        return new ResponseEntity<>(new UserData(),HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getOrders/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Set<Order>> getOrders(@PathVariable("id") Long id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isEmpty()) return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(user.get().getOrders(),HttpStatus.OK);
    }
}
