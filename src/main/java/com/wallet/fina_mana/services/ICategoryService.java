package com.wallet.fina_mana.services;

import com.wallet.fina_mana.Exceptions.DataNotFoundException;
import com.wallet.fina_mana.dtos.CategoryDTO;
import com.wallet.fina_mana.models.Category;
import com.wallet.fina_mana.repositories.CategoryRepository;
import com.wallet.fina_mana.responses.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ICategoryService {
    Category createCategory(long[] userId, CategoryDTO categoryDTO, boolean type) throws Exception;

    Category getCategoryById(long userId, long id,  boolean type) throws DataNotFoundException;

    List<CategoryResponse> getAllCategoryByType(long[] userId, boolean type) throws Exception;

    Category updateCategoryById(long[] userId, long id, CategoryDTO categoryDTO, boolean type) throws Exception;

    void deleteCategory(long id, long[] userId, boolean type) throws DataNotFoundException;

    List<Category> getAllCategoryIsNotChild(long[] userId, boolean type) throws Exception;

    List<Category> getChildByParent(long[] userId, long parentId,  boolean type);

}
