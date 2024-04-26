package com.wallet.fina_mana.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wallets")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wallet {
    @Id         // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY)         // Tự động tăng
    @Column(name = "id")                            // Đánh dâu ánh xạ tới cột id trong DB, tại đây ko có cũng đc vì tên cột là giống nhau
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "icon")
    private String icon;

    @Column(name = "money", nullable = false)
    private String money;

    @Column(name = "active")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "belong_user", nullable = false)
    private boolean belongUser;
}
