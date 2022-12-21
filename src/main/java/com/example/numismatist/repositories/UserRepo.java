package com.example.numismatist.repositories;

import com.example.numismatist.enteties.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
