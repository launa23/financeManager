package com.wallet.fina_mana.responses;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticByCategoryResponse {
    private long id;
    private String name;
    private String icon;
    private String total;
}
