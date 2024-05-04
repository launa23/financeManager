package com.wallet.fina_mana.responses;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticTransaction {
    private String date;
    private String totalIncome;
    private String totalOutcome;
}
