package com.vector.auto.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
public class AutopartName {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    private String name;
    private double price;
    

    public AutopartName(String name,double price) {
        this.name = name;
        this.price = price;
    }
}
