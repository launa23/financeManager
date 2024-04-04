package com.wallet.fina_mana.repositories;

import com.wallet.fina_mana.models.Category;
import com.wallet.fina_mana.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserId(long userId);

    Optional<Category> findByIdAndUserId(long id, long userId);

    Optional<Category> findByIdAndUserIdAndType(long id, long userId, boolean type);

    Optional<Category> findByUserIdAndIdAndActiveAndType(long userId, long id, boolean active, boolean type);

    boolean existsByUserIdAndNameAndActiveAndType(long userId, String name, boolean active, boolean type);


    Optional<Category> findByIdAndActiveAndType(long id, boolean active, boolean type);
    @Query(value = "SELECT * FROM categories WHERE id NOT IN\n" +
            "(SELECT child_id FROM category_hierarchy LEFT JOIN categories ON category_hierarchy.category_id = categories.id)\n" +
            "AND user_id IN :user AND active = 1 AND type = :type and categories.id = :id", nativeQuery = true)
    Optional<Category> findByParentId(@Param("user") long[] user, @Param("type") boolean type, @Param("id") long id);

    @Query(value = "SELECT * FROM categories c " +
            "WHERE c.user_id IN :user and c.name = :name and c.active = 1 and c.type = :type", nativeQuery = true)
    Optional<Category> existsByUserIdInAndNameAndActiveAndTypeLaun(@Param("user") long[] userId, @Param("name") String name, @Param("type") boolean type);

    @Query(value = "SELECT * FROM categories c " +
            "WHERE c.user_id IN :user and c.id = :id and c.active = 1 and c.type = :type", nativeQuery = true)
    Optional<Category> findByUserIdAndIdAndTypeLaun(@Param("user") long[] userId, @Param("id") long id, @Param("type") boolean type);
    @Query(value = "SELECT * FROM categories c WHERE c.name = :name and c.active = 1 and type = :type and c.id != :id and user_id in :user",
            nativeQuery = true)
    Category findDifferentIdAndSameName(@Param("id") long id, @Param("name") String name, @Param("type") boolean type, @Param("user") long[] userId);

    // Lấy danh mục con theo danh mục cha
    @Query(value = "SELECT c.* \n" +
            "FROM categories c\n" +
            "JOIN category_hierarchy ch ON c.id = ch.child_id\n" +
            "WHERE ch.category_id = :id and type = :type and user_id in :user and active = 1", nativeQuery = true)
    List<Category> findByParent(@Param("user") long[] user, @Param("id") long id, @Param("type") boolean type);

    // Lấy ra các danh mục không phải danh mục con
    @Query(value = "SELECT * FROM categories WHERE id NOT IN " +
            "(SELECT child_id FROM category_hierarchy LEFT JOIN categories ON category_hierarchy.category_id = categories.id) " +
            "AND user_id IN :user AND active = 1 AND type = :type", nativeQuery = true)
    List<Category> findCategoriesByUserIsNotChild(@Param("user") long[] user, @Param("type") boolean type);
}
