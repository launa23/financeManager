package com.wallet.fina_mana.services;

import com.wallet.fina_mana.Exceptions.DataNotFoundException;
import com.wallet.fina_mana.dtos.WalletDTO;
import com.wallet.fina_mana.models.Transaction;
import com.wallet.fina_mana.models.User;
import com.wallet.fina_mana.models.Wallet;
import com.wallet.fina_mana.repositories.TransactionRepository;
import com.wallet.fina_mana.repositories.UserRepository;
import com.wallet.fina_mana.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService implements IWalletService{
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    @Override
    public Wallet createWallet(WalletDTO walletDTO, User user, boolean belongUser) throws Exception {
        if (walletRepository.existsByUserIdAndNameAndActive(user.getId(), walletDTO.getName(), true)){
            throw new Exception("Wallet's name is already exist");
        }
        Wallet wallet = Wallet.builder()
                .name(walletDTO.getName())
                .icon(walletDTO.getIcon())
                .money(walletDTO.getMoney())
                .user(user)
                .active(true)
                .belongUser(belongUser)
                .build();
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet getWalletById(long id, long userId) throws DataNotFoundException {
        return walletRepository.findByIdAndUserIdAndActive(id, userId,  true)
                .orElseThrow(() -> new DataNotFoundException("Cannot find wallet id: " + id));
    }

    @Override
    public Wallet getFirstWalletByUserId(long userId) throws DataNotFoundException {
        return walletRepository.findFirstByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find wallet!"));
    }

    @Override
    public List<Wallet> getAllWallets(long userId) {
        return walletRepository.findByUserIdAndActive(userId, true);
    }

    @Override
    public Wallet updateWallet(long id, WalletDTO walletDTO, long userId) throws DataNotFoundException {
        Wallet existingWallet = walletRepository.findByIdAndUserIdAndActive(id, userId, true)
                .orElseThrow(() -> new DataNotFoundException("Cannot find wallet id: " + id));
        existingWallet.setName(walletDTO.getName());
        existingWallet.setIcon(walletDTO.getIcon());
        existingWallet.setMoney(walletDTO.getMoney());
        return walletRepository.save(existingWallet);
    }

    @Override
    public void deleteWallet(long id, long userId) throws DataNotFoundException {
        // Xóa cứng, xóa ví thì sẽ xoá luôn giao dịch trong ví
        Wallet existingWallet = walletRepository.findByIdAndUserIdAndActive(id, userId, true)
                .orElseThrow(() -> new DataNotFoundException("Cannot find wallet id: " + id));
        if(walletRepository.existsByIdAndUserIdAndActiveAndBelongUser(id, userId, true, false)){
            throw new DataNotFoundException("Unable to delete system wallet!");
        }
        List<Transaction> transactionList = transactionRepository.findByWalletId(id);
        if (!transactionList.isEmpty()){
            transactionRepository.deleteAllInBatch(transactionList);
        }
//        existingWallet.setActive(false);
        walletRepository.delete(existingWallet);
    }
}
