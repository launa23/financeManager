package com.wallet.fina_mana.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "users")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id         // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY)         // Tự động tăng
    @Column(name = "id")                            // Đánh dâu ánh xạ tới cột id trong DB, tại đây ko có cũng đc vì tên cột là giống nhau
    private Long id;

    @Column(name = "fullname", length = 100)
    private String fullName;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "active")
    private boolean active;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;
}
