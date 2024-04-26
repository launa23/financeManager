package com.wallet.fina_mana.responses;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private long id;
    private String fullName;
    private String phoneNumber;
    private Date dateOfBirth;
}
