package com.wallet.fina_mana.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category_hierarchy")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryHierarchy {
    @Id         // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY)         // Tự động tăng
    //@Column(name = "id")                            // Đánh dâu ánh xạ tới cột id trong DB, tại đây ko có cũng đc vì tên cột là giống nhau
    private Long id;

//    @Id
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category parent;

//    @Id
    @ManyToOne
    @JoinColumn(name = "child_id")
    private Category child;
}
