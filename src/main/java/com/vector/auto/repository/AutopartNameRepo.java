package com.vector.auto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vector.auto.model.AutopartName;

@Repository
public interface AutopartNameRepo extends JpaRepository<AutopartName,Long>{
    
}
