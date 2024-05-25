package com.vector.auto.model;



import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity(name="orders")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private Double totalCost;
    private Status status;

    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "id")
    private User users;



    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems;
}
