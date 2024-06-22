package com.vector.auto.model;


import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToOne;
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

    @ElementCollection
    @MapKeyColumn(name="title")
    @Column(name="description")
    private Map<String,String> specs;

    private String brand;
    @ElementCollection
    private List<String> images;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="name_id",referencedColumnName = "id")
    private AutopartName aName;

    @ManyToOne
    @JoinColumn(name="category_id",referencedColumnName = "id")
    private Category category;

    private Boolean brandNew;

    private Date createdDate;

    public Autopart(AutopartForm part) {
        this.setAName(new AutopartName(part.getName(),part.getPrice()));
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
