package com.example.OTP.repository;

import com.example.OTP.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    boolean existsByUserId(String userId);
}
