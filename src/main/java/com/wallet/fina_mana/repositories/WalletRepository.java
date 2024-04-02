package com.wallet.fina_mana.repositories;

import com.wallet.fina_mana.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByUserIdAndActive(long id, boolean active);

    boolean existsByUserIdAndNameAndActive(long userId, String name, boolean active);

    Optional<Wallet> findByIdAndActive(long id, boolean active);


    Optional<Wallet> findByNameAndActive(String name, boolean active);

    Optional<Wallet> findByUserIdAndIdAndActive(long userId, long id, boolean active);
}
