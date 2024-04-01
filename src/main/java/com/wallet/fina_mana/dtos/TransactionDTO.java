package com.wallet.fina_mana.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {
    @Min(value = 0, message = "Money must be >= 0")
    @JsonProperty("amount")
    private String amount;

    private String description;

    @JsonProperty("time")
    private Date time;

    @Min(value = 1, message = "Wallet ID must be > 0")
    @JsonProperty("wallet_id")
    private Long walletId;

    @Min(value = 1, message = "Category ID must be > 0")
    @JsonProperty("category_id")
    private Long categoryId;
}
