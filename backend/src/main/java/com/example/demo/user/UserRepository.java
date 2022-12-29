package com.example.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users u WHERE u.name = ?1", nativeQuery = true)
    User getUserByName(String name);

    @Query(value = "SELECT * FROM users u WHERE u.email = ?1", nativeQuery = true)
    User getUserByEmail(String email);

}
