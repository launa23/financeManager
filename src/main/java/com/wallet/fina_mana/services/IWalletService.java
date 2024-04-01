package com.wallet.fina_mana.services;

import com.wallet.fina_mana.Exceptions.DataNotFoundException;
import com.wallet.fina_mana.dtos.WalletDTO;
import com.wallet.fina_mana.models.Wallet;

import java.util.List;

public interface IWalletService {
    Wallet createWallet(WalletDTO walletDTO) throws Exception;

    Wallet getWalletById(long id) throws DataNotFoundException;

    List<Wallet> getAllWallets(long userId);

    Wallet updateWallet(long id, WalletDTO walletDTO) throws DataNotFoundException;

    void deleteWallet(long id) throws DataNotFoundException;
}
