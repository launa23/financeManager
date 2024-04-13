package com.wallet.fina_mana.responses;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransByDateResponse {
    private String time;
    List<TransactionResponse> transactionResponseList;
    public TransByDateResponse(String time) {
        this.time = time;
    }
}
