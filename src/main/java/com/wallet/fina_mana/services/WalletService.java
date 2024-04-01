package com.wallet.fina_mana.services;

import com.wallet.fina_mana.Exceptions.DataNotFoundException;
import com.wallet.fina_mana.dtos.WalletDTO;
import com.wallet.fina_mana.models.User;
import com.wallet.fina_mana.models.Wallet;
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
    @Override
    public Wallet createWallet(WalletDTO walletDTO) throws Exception {
        User user = userRepository.findById(walletDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user id: " + walletDTO.getUserId()));
        if (walletRepository.existsByUserIdAndNameAndActive(user.getId(), walletDTO.getName(), true)){
            throw new Exception("Wallet's name is already exist");
        }
        Wallet wallet = Wallet.builder()
                .name(walletDTO.getName())
                .icon(walletDTO.getIcon())
                .money(walletDTO.getMoney())
                .user(user)
                .active(true)
                .build();
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet getWalletById(long id) throws DataNotFoundException {
        Wallet existingWallet = walletRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new DataNotFoundException("Cannot find wallet id: " + id));
        return existingWallet;
    }

    @Override
    public List<Wallet> getAllWallets(long userId) {
        return walletRepository.findByUserIdAndActive(userId, true);
    }

    @Override
    public Wallet updateWallet(long id, WalletDTO walletDTO) throws DataNotFoundException {
        Wallet existingWallet = walletRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new DataNotFoundException("Cannot find wallet id: " + id));
        existingWallet.setName(walletDTO.getName());
        existingWallet.setIcon(walletDTO.getIcon());
        existingWallet.setMoney(walletDTO.getMoney());
        return walletRepository.save(existingWallet);
    }

    @Override
    public void deleteWallet(long id) throws DataNotFoundException {
        Wallet existingWallet = walletRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new DataNotFoundException("Cannot find wallet id: " + id));
        existingWallet.setActive(false);
        walletRepository.save(existingWallet);
    }
}
