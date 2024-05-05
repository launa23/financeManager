package com.wallet.fina_mana.services;

import com.wallet.fina_mana.dtos.TransactionDTO;
import com.wallet.fina_mana.models.Transaction;
import com.wallet.fina_mana.models.Wallet;
import com.wallet.fina_mana.responses.StatisticByCategoryResponse;
import com.wallet.fina_mana.responses.StatisticTransaction;
import com.wallet.fina_mana.responses.TransByDateResponse;
import com.wallet.fina_mana.responses.TransactionResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ITransactionService {
    Transaction createTransaction(long[] userId, TransactionDTO transactionDTO, boolean type) throws Exception;

    List<TransactionResponse> getAllTransactionsInWallet(long[] userId, long walletId) throws Exception;

    List<TransactionResponse> getAllTransactionsByUser(long userId) throws Exception;

    List<TransactionResponse> getAllTransactionsByUserAndType(long userId, boolean type) throws Exception;

    List<TransactionResponse> getTransactionInWalletByType(long userId, long walletId, boolean type) throws Exception;

    List<StatisticTransaction> getStatisticTransaction(long userId) throws Exception;
    List<StatisticTransaction> getStatisticTransactionByMonth(long userId) throws Exception;
    List<StatisticTransaction> getStatisticTransactionByYear(long userId) throws Exception;
    List<StatisticByCategoryResponse> getStatisticByCategory(long userId, String start, String end, boolean type) throws Exception;
    Transaction updateTransaction(long[] userId, long id, TransactionDTO transactionDTO, boolean type) throws Exception;
    List<TransByDateResponse> getByMonthAndYear(long userId, int month, int year, long walletId) throws Exception;
    Map<String, String> getTotalIncomeAndOutcome(long userId, int month, int year, long walletId) throws Exception;
    void deleteTransaction(long userId, long id, boolean type) throws Exception;
}
