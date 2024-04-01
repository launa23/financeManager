package com.wallet.fina_mana.repositories;

import com.wallet.fina_mana.models.Category;
import com.wallet.fina_mana.models.CategoryHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryHierarchyRepository extends JpaRepository<CategoryHierarchy, Long> {

    @Query(value = "SELECT category_hierarchy.* FROM category_hierarchy INNER JOIN categories " +
            "ON categories.id = category_hierarchy.child_id " +
            "WHERE category_hierarchy.child_id = :childId", nativeQuery = true)
    CategoryHierarchy findChildExistInHierarchy(@Param("childId") long childId);
}
