package com.wallet.fina_mana.controllers;

import com.wallet.fina_mana.dtos.TransactionDTO;
import com.wallet.fina_mana.models.Transaction;
import com.wallet.fina_mana.responses.TransactionResponse;
import com.wallet.fina_mana.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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
            long[] userIds = new long[]{0, 1};
            List<TransactionResponse> transactionResponses = transactionService.getAllTransactionsInWallet(userIds, walletId);
            return ResponseEntity.ok(transactionResponses);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTransaction(){
        try {
            long[] userIds = new long[]{0, 1};
            List<TransactionResponse> transactionResponses = transactionService.getAllTransactionsByUser(userIds[1]);
            return ResponseEntity.ok(transactionResponses);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> getTransactionByType(@PathVariable("type") String type){
        return ResponseEntity.ok("Lấy ra các giao dịch theo loại");
    }

    @GetMapping("/wallet/{walletId}/{type}")
    public ResponseEntity<?> getTransactionById(@PathVariable("type") String type,
                                                @PathVariable("walletId") long walletId){
        try {
            long[] userIds = new long[]{0, 1};
            boolean typeBoolean = type.equals("income");

            List<TransactionResponse> transactionResponses = transactionService.getTransactionInWalletByType(userIds[1], walletId, typeBoolean);
            return ResponseEntity.ok(transactionResponses);

        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("update/{type}/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable("id") long id,
                                               @PathVariable("type") String type,
                                               @Valid @RequestBody TransactionDTO transactionDTO,
                                               BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMess = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMess);
            }
            long[] userIds = new long[]{0, 1};
            boolean typeBoolean = type.equals("income");

            Transaction transaction = transactionService.updateTransaction(userIds, id, transactionDTO, typeBoolean);
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

    @PutMapping("delete/{type}/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable("id") long id,
                                               @PathVariable("type") String type){
        try {
            long[] userIds = new long[]{0, 1};
            boolean typeBoolean = type.equals("income");

            transactionService.deleteTransaction(userIds[1], id, typeBoolean);
            return ResponseEntity.ok("Xóa thành công: " + id);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
