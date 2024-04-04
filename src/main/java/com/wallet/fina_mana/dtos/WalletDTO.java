package com.wallet.fina_mana.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletDTO {
    @NotBlank(message = "Category's name cannot empty")
    @JsonProperty("name")
    private String name;

    @Min(value = 0, message = "Money must be >= 0")
    @JsonProperty("money")
    private String money;

//    @NotBlank(message = "Category's icon cannot empty")
    @JsonProperty("icon")
    private String icon;

//    @JsonProperty("user_id")
//    private long userId;
}
