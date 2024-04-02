package com.wallet.fina_mana.repositories;

import com.wallet.fina_mana.models.Category;
import com.wallet.fina_mana.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletIdAndActive(long walletId, boolean active);

    List<Transaction> findByWalletIdAndTypeAndActive(long walletId, boolean type, boolean active);

    List<Transaction> findByWallet_UserIdAndActive(Long wallet_user_id, boolean active);

    Optional<Transaction> findByIdAndWallet_UserIdAndActiveAndType(long id, Long wallet_user_id, boolean active, boolean type);

}