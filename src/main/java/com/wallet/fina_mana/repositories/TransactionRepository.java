package com.wallet.fina_mana.repositories;

import com.wallet.fina_mana.models.Category;
import com.wallet.fina_mana.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletIdAndActive(long walletId, boolean active);

    List<Transaction> findByWalletId(long walletId);

    List<Transaction> findByWallet_UserIdAndTypeAndActive(long userId, boolean type, boolean active);

    List<Transaction> findByWalletIdAndTypeAndActive(long walletId, boolean type, boolean active);

    List<Transaction> findByWallet_UserIdAndActive(Long wallet_user_id, boolean active);

    Optional<Transaction> findByIdAndWallet_UserIdAndActiveAndType(long id, Long wallet_user_id, boolean active, boolean type);

    @Query(value = "select tr.* from transactions as tr inner join wallets as w on w.id = tr.wallet_id" +
            " where w.user_id = :user_id and tr.wallet_id = :wallet_id and MONTH(tr.time) = :month and YEAR(tr.time) = :year and tr.active = 1 order by tr.time desc", nativeQuery = true)
    List<Transaction> findByMonthAndYear(@Param("month") int month, @Param("year") int year, @Param("user_id") long user_id, @Param("wallet_id") long walletId);
}