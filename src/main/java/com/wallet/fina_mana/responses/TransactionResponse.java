package com.wallet.fina_mana.responses;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private long id;
    private String amount;
    private String description;
    private Date time;
    private String type;
    private String catgoryName;
    private String walletName;
}
