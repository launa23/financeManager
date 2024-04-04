package com.wallet.fina_mana.services;

import com.wallet.fina_mana.dtos.TransactionDTO;
import com.wallet.fina_mana.models.Transaction;
import com.wallet.fina_mana.models.Wallet;
import com.wallet.fina_mana.responses.TransactionResponse;

import java.util.List;

public interface ITransactionService {
    Transaction createTransaction(long[] userId, TransactionDTO transactionDTO, boolean type) throws Exception;

    List<TransactionResponse> getAllTransactionsInWallet(long[] userId, long walletId) throws Exception;

    List<TransactionResponse> getAllTransactionsByUser(long userId) throws Exception;

    List<TransactionResponse> getAllTransactionsByUserAndType(long userId, boolean type) throws Exception;

    List<TransactionResponse> getTransactionInWalletByType(long userId, long walletId, boolean type) throws Exception;

    Transaction updateTransaction(long[] userId, long id, TransactionDTO transactionDTO, boolean type) throws Exception;

    void deleteTransaction(long userId, long id, boolean type) throws Exception;
}
