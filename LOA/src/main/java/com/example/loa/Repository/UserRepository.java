package com.example.loa.Repository;

import com.example.loa.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserId(String userid);

    Optional<User> findById(Integer id);
}
