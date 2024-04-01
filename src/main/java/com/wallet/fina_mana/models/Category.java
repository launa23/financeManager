package com.wallet.fina_mana.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id         // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY)         // Tự động tăng
    @Column(name = "id")                            // Đánh dâu ánh xạ tới cột id trong DB, tại đây ko có cũng đc vì tên cột là giống nhau
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "icon", nullable = false)
    private String icon;

    @Column(name = "type", nullable = false)
    private boolean type;

    @Column(name = "active")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToMany
//    @JoinColumn(name = "child", nullable = true)
//    private List<Category> child;

//    public void addChildCategory(Category category) {
//        this.child.add(category);
//    }
}
