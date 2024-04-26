package com.wallet.fina_mana.responses;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletResponse {
    private long id;
    private String name;
    private String icon;
    private String money;
    private boolean belongUser;
//    private long userId;
}
