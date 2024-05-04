package com.wallet.fina_mana.repositories;

import com.wallet.fina_mana.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByUserIdAndActive(long id, boolean active);

    Optional<Wallet> findByUserIdAndNameAndActive(long userId, String name,  boolean active);

    boolean existsByUserIdAndNameAndActive(long userId, String name, boolean active);

    boolean existsByIdAndUserIdAndActiveAndBelongUser(long id, long userId, boolean active, boolean belongUser);

    Optional<Wallet> findByIdAndActive(long id, boolean active);

    Optional<Wallet> findByIdAndUserIdAndActive(long id, long userId, boolean active);

    Optional<Wallet> findByNameAndActive(String name, boolean active);

    Optional<Wallet> findByUserIdAndIdAndActive(long userId, long id, boolean active);

    @Query(value = "SELECT * from wallets WHERE wallets.user_id = :userId and active = 1 LIMIT 1", nativeQuery = true)
    Optional<Wallet> findFirstByUserId(long userId);
    @Query(value = "SELECT * FROM wallets as w WHERE w.name = :name and active = 1 and user_id = :userId and id != :id", nativeQuery = true)
    Wallet findDifferentIdAndSameName(@Param("name") String name, @Param("userId") long userId, @Param("id") long id);
}
