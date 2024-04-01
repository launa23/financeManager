package com.wallet.fina_mana.services;

import com.wallet.fina_mana.dtos.TransactionDTO;
import com.wallet.fina_mana.models.Transaction;
import com.wallet.fina_mana.models.Wallet;
import com.wallet.fina_mana.responses.TransactionResponse;

import java.util.List;

public interface ITransactionService {
    Transaction createTransaction(long[] userId, TransactionDTO transactionDTO, boolean type) throws Exception;

    List<TransactionResponse> getAllTransactionsInWallet(long[] userId, long walletId) throws Exception;

    List<Transaction> getTransactionInWalletByType(long walletId, boolean type) throws Exception;

    Transaction updateTransaction(long id, TransactionDTO transactionDTO) throws Exception;

    void deleteTransaction(long id) throws Exception;
}
