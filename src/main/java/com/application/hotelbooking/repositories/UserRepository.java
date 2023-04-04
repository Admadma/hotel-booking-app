package com.application.hotelbooking.repositories;

import com.application.hotelbooking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(Long id);

    List<User> findUserByUsername(String username);
}
