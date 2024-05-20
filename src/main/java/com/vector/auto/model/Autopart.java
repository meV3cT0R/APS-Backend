package com.vector.auto.model;


import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Autopart {
    private @Id @GeneratedValue(strategy=GenerationType.AUTO) Long id; 
    private String name;

    @ElementCollection
    @MapKeyColumn(name="title")
    @Column(name="description")
    private Map<String,String> specs;

    private double price;
    private String category;
    private String brand;
    @ElementCollection
    private List<String> images;

    public Autopart(AutopartForm part) {
        this.name = part.getName();
        this.specs = part.getSpecs();
        this.price = part.getPrice();
        this.category = part.getCategory();
        this.images = part.getImages();
    }
}
