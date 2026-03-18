package com.dubanjang.ieum.user.domain.repository;

import com.dubanjang.ieum.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByEmail(String email);
    Optional<User> findByEmail(String email);
}
