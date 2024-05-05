package com.wallet.fina_mana.responses;

import com.wallet.fina_mana.models.Category;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticByCategory {
    private Category category;
    private String total;
}
