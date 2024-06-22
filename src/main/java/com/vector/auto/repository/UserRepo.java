package com.vector.auto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vector.auto.model.User;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    public User findByUsernameAndPassword(String username,String password);
    public Optional<User> findByUsername(String username);

    public Optional<User> findByEmail(String email);

    public Optional<User> findByUsernameOrEmail(String username,String email);
}
