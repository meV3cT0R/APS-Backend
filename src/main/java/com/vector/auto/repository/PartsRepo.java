package com.vector.auto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vector.auto.model.Autopart;

@Repository
public interface PartsRepo extends JpaRepository<Autopart,Long>{

    @Query(value="SELECT distinct u.brand FROM Autopart u",nativeQuery = true)
    List<String> getAllBrands();

    @Query(value="SELECT * from Autopart order by created_date DESC limit 10",nativeQuery = true)
    List<Autopart> getLatestAutoparts();
} 
