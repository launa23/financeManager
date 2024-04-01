package com.wallet.fina_mana.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "transactions")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    @Id         // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY)         // Tự động tăng
    @Column(name = "id")                            // Đánh dâu ánh xạ tới cột id trong DB, tại đây ko có cũng đc vì tên cột là giống nhau
    private Long id;

    @Column(name = "amount", nullable = false)
    private String amount;

    @Column(name = "active")
    private boolean active;

    @Column(name = "description")
    private String description;

    @Column(name = "time", nullable = false)
    private Date time;

    @Column(name = "type", nullable = false)
    private boolean type;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
}
