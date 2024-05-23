package com.vector.auto.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id; 
    private String name;
    private String username;
    private String password;
    private Role role;
    private String Address;

    @OneToMany(mappedBy="users",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private Set<Order> orders;
}
