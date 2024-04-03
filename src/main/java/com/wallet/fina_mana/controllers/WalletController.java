package com.wallet.fina_mana.controllers;

import com.wallet.fina_mana.dtos.CategoryDTO;
import com.wallet.fina_mana.dtos.WalletDTO;
import com.wallet.fina_mana.models.Wallet;
import com.wallet.fina_mana.responses.WalletResponse;
import com.wallet.fina_mana.services.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/wallet")
public class WalletController {
    private final WalletService walletService;
    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@Valid @RequestBody WalletDTO walletDTO,
                                            BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMess = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMess);
            }
            Wallet wallet = walletService.createWallet(walletDTO);
            WalletResponse walletResponse = WalletResponse.builder()
                    .id(wallet.getId())
                    .name(wallet.getName())
                    .money(wallet.getMoney())
                    .userId(wallet.getUser().getId())
                    .build();
            return ResponseEntity.ok(walletResponse);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAllWallet(@PathVariable("id") long userId){
        List<Wallet> walletList = walletService.getAllWallets(userId);
        List<WalletResponse> walletResponses = walletList.stream()
                .map(wallet -> WalletResponse.builder()
                        .id(wallet.getId())
                        .name(wallet.getName())
                        .money(wallet.getMoney())
                        .build()).toList();
        return ResponseEntity.ok(walletResponses.size() != 0 ? walletResponses : "Bạn chưa có ví nào!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWalletById(@PathVariable("id") long id){
        try {
            Wallet wallet = walletService.getWalletById(id);
            WalletResponse walletResponse = WalletResponse.builder()
                    .id(wallet.getId())
                    .name(wallet.getName())
                    .money(wallet.getMoney())
                    .userId(wallet.getUser().getId())
                    .build();

            return ResponseEntity.ok(walletResponse);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateWallet(@PathVariable("id") long id,
                                            @Valid @RequestBody WalletDTO walletDTO,
                                            BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMess = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMess);
            }
            Wallet wallet = walletService.updateWallet(id, walletDTO);

            WalletResponse walletResponse = WalletResponse.builder()
                    .id(wallet.getId())
                    .name(wallet.getName())
                    .money(wallet.getMoney())
                    .userId(wallet.getUser().getId())
                    .build();
            return ResponseEntity.ok(walletResponse);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWallet(@PathVariable("id") long id){
        try {
            walletService.deleteWallet(id);
            return ResponseEntity.ok("Delete successfully!");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
