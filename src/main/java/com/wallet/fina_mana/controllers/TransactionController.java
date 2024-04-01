package com.wallet.fina_mana.controllers;

import com.wallet.fina_mana.dtos.TransactionDTO;
import com.wallet.fina_mana.models.Transaction;
import com.wallet.fina_mana.responses.TransactionResponse;
import com.wallet.fina_mana.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/create/{type}")
    public ResponseEntity<?> createTransaction(@PathVariable("type") String type,
                                               @Valid @RequestBody TransactionDTO transactionDTO,
                                               BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMess = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMess);
            }
            long[] userIds = new long[]{0, 1};
            boolean typeBoolean = type.equals("income");

            Transaction transaction = transactionService.createTransaction(userIds, transactionDTO, typeBoolean);
            TransactionResponse transactionResponse = TransactionResponse.builder()
                    .id(transaction.getId())
                    .amount(transaction.getAmount())
                    .time(transaction.getTime())
                    .description(transaction.getDescription())
                    .type(transaction.isType() ? "Income" : "Outcome")
                    .catgoryName(transaction.getCategory().getName())
                    .walletName(transaction.getWallet().getName())
                    .build();
            return ResponseEntity.ok(transactionResponse);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<?> getTransactionInWallet(@PathVariable("walletId") long walletId){
        try {
            long[] userIds = new long[]{0, 2};
            List<TransactionResponse> transactionResponses = transactionService.getAllTransactionsInWallet(userIds, walletId);
            return ResponseEntity.ok(transactionResponses);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTransaction(){
        return ResponseEntity.ok("Lấy ra tất cả các giao dịch");
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> getTransactionByType(@PathVariable("type") String type){
        return ResponseEntity.ok("Lấy ra các giao dịch theo loại");
    }

    @GetMapping("/{type}/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable("type") String type,
                                                @PathVariable("id") long id){
        return ResponseEntity.ok("Lấy ra các giao dịch theo ID");
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable("id") long id,
                                               @Valid @RequestBody TransactionDTO transactionDTO,
                                               BindingResult result){
        if (result.hasErrors()){
            List<String> errorMess = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMess);
        }
        return ResponseEntity.ok("Sửa giao dịch thành công: " + transactionDTO);
    }

    @PutMapping("delete/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable("id") long id){
        return ResponseEntity.ok("Xóa giao dịch thành công: " + id);
    }
}
