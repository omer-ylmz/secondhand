package com.omer.secondhand.user.repository;

import com.omer.secondhand.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
