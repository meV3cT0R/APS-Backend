package com.vector.auto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vector.auto.model.CartItem;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Long> {
    
}
