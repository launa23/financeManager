package com.wallet.fina_mana.repositories;

import com.wallet.fina_mana.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String usename);
//    User findById(long id);
    Optional<User> findByUsername(String username);
}
