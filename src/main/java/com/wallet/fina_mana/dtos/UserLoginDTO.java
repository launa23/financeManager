package com.wallet.fina_mana.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDTO {
    @JsonProperty("username")
    @NotBlank(message = "Username is required")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;
}
