package com.wallet.fina_mana.controllers;

import com.wallet.fina_mana.dtos.CategoryDTO;
import com.wallet.fina_mana.dtos.WalletDTO;
import com.wallet.fina_mana.models.User;
import com.wallet.fina_mana.models.Wallet;
import com.wallet.fina_mana.responses.WalletResponse;
import com.wallet.fina_mana.services.UserService;
import com.wallet.fina_mana.services.WalletService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final UserService userService;
    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@Valid @RequestBody WalletDTO walletDTO,
                                            HttpServletRequest request,
                                            BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMess = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMess);
            }
            User user = userService.getCurrent(request);
            Wallet wallet = walletService.createWallet(walletDTO, user);
            WalletResponse walletResponse = WalletResponse.builder()
                    .id(wallet.getId())
                    .name(wallet.getName())
                    .money(wallet.getMoney())
                    .icon(wallet.getIcon())
//                    .userId(wallet.getUser().getId())
                    .build();
            return ResponseEntity.ok(walletResponse);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/mine")
    public ResponseEntity<?> getAllWallet(HttpServletRequest request){
        try {
            long userId = userService.getCurrent(request).getId();
            List<Wallet> walletList = walletService.getAllWallets(userId);
            List<WalletResponse> walletResponses = walletList.stream()
                    .map(wallet -> WalletResponse.builder()
                            .id(wallet.getId())
                            .name(wallet.getName())
                            .money(wallet.getMoney())
                            .icon(wallet.getIcon())
                            .build()).toList();
            return ResponseEntity.ok(!walletResponses.isEmpty() ? walletResponses : "you don't have a wallet!");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/first")
    public ResponseEntity<?> getFirstWallet(HttpServletRequest request){
        try {
            long userId = userService.getCurrent(request).getId();
            Wallet wallet = walletService.getFirstWalletByUserId(userId);
            WalletResponse walletResponse = WalletResponse.builder()
                    .id(wallet.getId())
                    .name(wallet.getName())
                    .money(wallet.getMoney())
                    .icon(wallet.getIcon())
                    .build();
            return ResponseEntity.ok(walletResponse);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWalletById(@PathVariable("id") long id, HttpServletRequest request){
        try {
            long userId = userService.getCurrent(request).getId();
            Wallet wallet = walletService.getWalletById(id, userId);
            WalletResponse walletResponse = WalletResponse.builder()
                    .id(wallet.getId())
                    .name(wallet.getName())
                    .money(wallet.getMoney())
                    .icon(wallet.getIcon())
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
                                            HttpServletRequest request,
                                            BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMess = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMess);
            }
            long userId = userService.getCurrent(request).getId();
            Wallet wallet = walletService.updateWallet(id, walletDTO, userId);

            WalletResponse walletResponse = WalletResponse.builder()
                    .id(wallet.getId())
                    .name(wallet.getName())
                    .money(wallet.getMoney())
                    .icon(wallet.getIcon())
                    .build();
            return ResponseEntity.ok(walletResponse);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWallet(@PathVariable("id") long id, HttpServletRequest request){
        try {
            long userId = userService.getCurrent(request).getId();
            walletService.deleteWallet(id, userId);
            return ResponseEntity.ok("Delete successfully!");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
