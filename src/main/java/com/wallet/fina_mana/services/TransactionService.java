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
import java.util.Objects;

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
    public List<TransactionResponse> getAllTransactionsByUser(long userId) throws Exception {
        return transactionRepository.findByWallet_UserIdAndActive(userId, true)
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
    }

    @Override
    public List<TransactionResponse> getAllTransactionsByUserAndType(long userId, boolean type) throws Exception {
        return transactionRepository.findByWallet_UserIdAndTypeAndActive(userId, type, true)
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
    }


    @Override
    public List<TransactionResponse> getTransactionInWalletByType(long userId, long walletId, boolean type) throws Exception {
        Wallet wallet = walletRepository.findByUserIdAndIdAndActive(userId, walletId, true)
                .orElseThrow(() -> new DataNotFoundException("Cannot find wallet: " + walletId));

        return transactionRepository.findByWalletIdAndTypeAndActive(walletId, type, true)
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
    }

    @Override
    public Transaction updateTransaction(long[] userId, long id, TransactionDTO transactionDTO, boolean type) throws Exception {
        Transaction transaction = transactionRepository.findByIdAndWallet_UserIdAndActiveAndType(id, userId[1], true , type)
                .orElseThrow(() -> new DataNotFoundException("Cannot find transaction: " + id));
        Wallet wallet = walletRepository.findByUserIdAndIdAndActive( userId[1], transactionDTO.getWalletId(),true)
                .orElseThrow(() -> new DataNotFoundException("Cannot find wallet: " + transactionDTO.getWalletId()));
        Category category = categoryRepository.findByUserIdAndIdAndTypeLaun(userId, transactionDTO.getCategoryId(), type)
                .orElseThrow(() -> new DataNotFoundException("Cannot find category: " + transactionDTO.getCategoryId()));

        float oldAmount = Float.parseFloat(transaction.getAmount());
        float newAmount = Float.parseFloat(transactionDTO.getAmount());
        float moneyInWallet = Float.parseFloat(wallet.getMoney());
        // Trong trường hợp không đổi ví
        if (Objects.equals(wallet.getId(), transaction.getWallet().getId())) {
            // Type = income
            if (type){
                moneyInWallet += (newAmount - oldAmount);
                wallet.setMoney(Float.toString(moneyInWallet));
            }
            // Type = outcome
            else {
                moneyInWallet -= (newAmount - oldAmount);
                wallet.setMoney(Float.toString(moneyInWallet));
            }
        }
        // Trong trường hợp đổi ví
        else {
            Wallet oldWallet = walletRepository.findByUserIdAndIdAndActive( userId[1], transaction.getWallet().getId(),true)
                    .orElseThrow(() -> new DataNotFoundException("Cannot find wallet: " + transaction.getWallet().getId()));
            float moneyInOld = Float.parseFloat(oldWallet.getMoney());
            if (type){
                moneyInOld -= oldAmount;
                oldWallet.setMoney(Float.toString(moneyInOld));
                moneyInWallet += newAmount;
                wallet.setMoney(Float.toString(moneyInWallet));
            }
            // Type = outcome
            else {
                moneyInOld += oldAmount;
                oldWallet.setMoney(Float.toString(moneyInOld));
                moneyInWallet -= newAmount;
                wallet.setMoney(Float.toString(moneyInWallet));
            }
            walletRepository.save(oldWallet);
        }
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription(transaction.getDescription());
        transaction.setTime(transactionDTO.getTime());
        transaction.setCategory(category);
        transaction.setWallet(wallet);

        walletRepository.save(wallet);
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(long userId, long id, boolean type) throws Exception {
        Transaction transaction = transactionRepository.findByIdAndWallet_UserIdAndActiveAndType(id, userId, true, type)
                .orElseThrow(() -> new DataNotFoundException("Cannot find transaction: " + id));
        Wallet wallet = walletRepository.findByUserIdAndIdAndActive( userId, transaction.getWallet().getId(),true)
                .orElseThrow(() -> new DataNotFoundException("Cannot find wallet: " + transaction.getWallet().getId()));
        float oldAmount = Float.parseFloat(transaction.getAmount());
        float moneyInWallet = Float.parseFloat(wallet.getMoney());
        // Type = income
        if (type){
            moneyInWallet -= oldAmount;
            wallet.setMoney(Float.toString(moneyInWallet));
        }
        // Type = outcome
        else {
            moneyInWallet += oldAmount;
            wallet.setMoney(Float.toString(moneyInWallet));
        }
        walletRepository.save(wallet);
        transaction.setActive(false);
        transactionRepository.save(transaction);
    }
}
