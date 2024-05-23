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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.vector.auto.repository.PartsRepo;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private PartsRepo partsRepo;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/images/products";

    @DeleteMapping("deleteProduct/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Autopart> deleteProduct(@PathVariable("id") Long id) {
        Optional<Autopart> oAutoPart = partsRepo.findById(id);
        if (oAutoPart.isEmpty())
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        for(String url : oAutoPart.get().getImages()) {
            File file = new File(System.getProperty("user.dir")+"/src/main/resources"+url);
            if(file.exists())
                file.delete();
        }
        partsRepo.delete(oAutoPart.get());
        return new ResponseEntity<>(oAutoPart.get(),HttpStatus.OK);
    }


    @PostMapping("updateProduct/{id}") 
    @PreAuthorize("hasAuthority('ADMIN')")
    public Autopart updateProduct(@ModelAttribute AutopartForm prod, @RequestParam("specs") String specs,
            @RequestParam("imageList") MultipartFile[] files, @PathVariable("id") Long id) throws IOException {
        Optional<Autopart> oAutoPart = partsRepo.findById(id);
        if (oAutoPart.isEmpty())
            return new Autopart();
        Autopart autopart = oAutoPart.get();
        if (prod.getName() != null && !prod.getName().trim().isEmpty())
            autopart.setName(prod.getName());
        if (prod.getCategory() != null && !prod.getCategory().trim().isEmpty())
            autopart.setCategory(prod.getCategory());

        if (prod.getPrice() != null)
            autopart.setPrice(prod.getPrice());

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
        for (MultipartFile file : files) {
            images.add(String.format("%s/%s", "/images/products", file.getOriginalFilename()));
            Path fileAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());

            if (!fileAndPath.toFile().exists())
                Files.write(fileAndPath, file.getBytes());
            /*
             * TODO : suppose different product has same image name then later one won't be
             * uploaded so fix it
             */
        }
        autopart.setImages(images);
        return partsRepo.save(autopart);
    }

    @PostMapping("/saveProduct")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Autopart saveProduct(@ModelAttribute AutopartForm prod, @RequestParam("specs") String specs,
            @RequestParam("imageList") MultipartFile[] files) throws IOException {
        Autopart part = new Autopart(prod);

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

}
