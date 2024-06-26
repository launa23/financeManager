package com.wallet.fina_mana.services;

import com.wallet.fina_mana.Exceptions.DataNotFoundException;
import com.wallet.fina_mana.dtos.TransactionDTO;
import com.wallet.fina_mana.models.Category;
import com.wallet.fina_mana.models.Transaction;
import com.wallet.fina_mana.models.Wallet;
import com.wallet.fina_mana.repositories.CategoryRepository;
import com.wallet.fina_mana.repositories.TransactionRepository;
import com.wallet.fina_mana.repositories.WalletRepository;
import com.wallet.fina_mana.responses.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService{
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Transaction createTransaction(long[] userId, TransactionDTO transactionDTO, boolean type) throws Exception {
        Wallet wallet = walletRepository.findByUserIdAndIdAndActive(userId[1], transactionDTO.getWalletId(), true)
                .orElseThrow(() -> new DataNotFoundException("Cannot find wallet: " + transactionDTO.getWalletId()));
        Category category = categoryRepository.findByUserIdAndIdAndTypeLaun(userId, transactionDTO.getCategoryId(), type)
                .orElseThrow(() -> new DataNotFoundException("Cannot find category: " + transactionDTO.getCategoryId()));

        // Ép kiểu thành kiểu số để tính lại số tiền trong ví
        long MoneyInWallet = Long.parseLong(wallet.getMoney());
        long amount = Long.parseLong(transactionDTO.getAmount());

        if (type){
            MoneyInWallet += amount;
        }
        else {
            MoneyInWallet -=amount;
        }
        Transaction transaction = Transaction.builder()
                .description(transactionDTO.getDescription())
                .active(true)
                .amount(Long.toString(amount))
                .time(transactionDTO.getTime())
                .type(type)
                .category(category)
                .wallet(wallet)
                .build();
        wallet.setMoney(Long.toString(MoneyInWallet));
        walletRepository.save(wallet);
        return transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionResponse> getAllTransactionsInWallet(long[] userId, long walletId) throws Exception {
        Wallet wallet = walletRepository.findByUserIdAndIdAndActive(userId[1], walletId, true)
                .orElseThrow(() -> new DataNotFoundException("Cannot find wallet: " + walletId));
        List<TransactionResponse> transactions = transactionRepository.findByWalletIdAndActive(walletId, true)
                .stream().map(transaction -> TransactionResponse.builder()
                        .id(transaction.getId())
                        .amount(transaction.getAmount())
                        .time(transaction.getTime())
                        .description(transaction.getDescription())
                        .type(transaction.isType() ? "Income" : "Outcome")
                        .catgoryName(transaction.getCategory().getName())
                        .walletName(transaction.getWallet().getName())
                        .build())
                .toList();
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionInWalletByType(long walletId, boolean type) throws Exception {
        return null;
    }

    @Override
    public Transaction updateTransaction(long id, TransactionDTO transactionDTO) throws Exception {
        return null;
    }

    @Override
    public void deleteTransaction(long id) throws Exception {

    }
}
