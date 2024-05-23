package com.vector.auto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vector.auto.model.Category;

@Repository
public interface CatRepo extends JpaRepository<Category,Long> {

}
