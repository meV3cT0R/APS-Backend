package com.vector.auto.model;


import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.PrePersist;
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
    private @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id; 
    private String name;

    @ElementCollection
    @MapKeyColumn(name="title")
    @Column(name="description")
    private Map<String,String> specs;

    private double price;
    private String brand;
    @ElementCollection
    private List<String> images;


    @ManyToOne
    @JoinColumn(name="category_id",referencedColumnName = "id")
    private Category category;

    private Boolean brandNew;

    private Date createdDate;

    public Autopart(AutopartForm part) {
        this.name = part.getName();
        this.price = part.getPrice();
        this.images = part.getImages();
        this.brandNew = part.getBrandNew();
        this.brand = part.getBrand();
    }

    @PrePersist
    protected void onCreate() {
        if(createdDate==null) 
            createdDate = new Date();
    } 
}
