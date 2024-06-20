package com.vector.auto.model;



import java.util.Date;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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

    private Date checkedOutDate;

    @PrePersist
    protected void onCreate() {
        if (this.checkedOutDate == null) {
            this.checkedOutDate = new Date();
        }
    }
}
