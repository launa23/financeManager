package com.wallet.fina_mana.services;

import com.wallet.fina_mana.Exceptions.DataNotFoundException;
import com.wallet.fina_mana.dtos.CategoryDTO;
import com.wallet.fina_mana.models.Category;
import com.wallet.fina_mana.models.CategoryHierarchy;
import com.wallet.fina_mana.models.User;
import com.wallet.fina_mana.repositories.CategoryHierarchyRepository;
import com.wallet.fina_mana.repositories.CategoryRepository;
import com.wallet.fina_mana.repositories.UserRepository;
import com.wallet.fina_mana.responses.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryHierarchyRepository categoryHierarchyRepository;
    @Override
    public Category createCategory(long userId, CategoryDTO categoryDTO, boolean type) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find user id: " + userId));
        if (categoryRepository.existsByUserIdAndNameAndActiveAndType(userId, categoryDTO.getName(), true, type)){
            throw new Exception("Category is already exist");
        }
        if (categoryDTO.getParentId() == null || categoryDTO.getParentId().isEmpty()){
            Category category = Category.builder()
                    .name(categoryDTO.getName())
                    .icon(categoryDTO.getIcon())
                    .type(type)
                    .active(true)
                    .user(user)
                    .build();
            return categoryRepository.save(category);
        }
        else{
            Category categoryParent = categoryRepository
                    .findByIdAndActiveAndType(Long.parseLong(categoryDTO.getParentId()),true, type)
                    .orElseThrow(() -> new DataNotFoundException("Cannot find category parent: " + categoryDTO.getParentId()));
            Category category = categoryRepository.save(Category.builder()
                    .name(categoryDTO.getName())
                    .icon(categoryDTO.getIcon())
                    .type(type)
                    .active(true)
                    .user(user)
                    .build());

            CategoryHierarchy categoryHierarchy = CategoryHierarchy.builder()
                    .parent(categoryParent)
                    .child(category)
                    .build();
            categoryHierarchyRepository.save(categoryHierarchy);
            return category;

        }
    }

    @Override
    public Category getCategoryById(long userId, long id, boolean type) throws DataNotFoundException {

        return categoryRepository.findByIdAndActiveAndType(id, true, type)
                .orElseThrow(() -> new DataNotFoundException("Cannot find category: " + id));
    }

    @Override
    public List<CategoryResponse> getAllCategoryByType(long[] userId, boolean type){
        // Lấy ra các category không phải là con
        List<CategoryResponse> categoriesParent = getAllCategoryIsNotChild(userId, type).stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .icon(category.getIcon())
                        .categoryOf(category.getUser() != null ? "User" : "System")
                        .build()).toList();
        // Lặp qua danh sách category trên, để lấy category con
        for (CategoryResponse c : categoriesParent) {
            List<CategoryResponse> categoriesChild = getChildByParent(userId, c.getId(), type).stream()
                    .map(category -> CategoryResponse.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .icon(category.getIcon())
                            .categoryOf(category.getUser() != null ? "User" : "System")
                            .build()).toList();

            c.setCategoryChilds(categoriesChild);
        }
        return categoriesParent;
    }

    @Override
    public Category updateCategoryById(long userId, long id, CategoryDTO categoryDTO, boolean type) throws Exception {
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find category: " + id));

        if (categoryRepository.findDifferentIdAndSameName(id, categoryDTO.getName(), true) != null){
            throw new Exception("Category is already exist");
        }
        category.setName(categoryDTO.getName());
        category.setIcon(categoryDTO.getIcon());

        if (categoryDTO.getParentId() == null || categoryDTO.getParentId().isEmpty()){
            Optional<CategoryHierarchy> categoryHierarchy = Optional.ofNullable(categoryHierarchyRepository.findChildExistInHierarchy(id));
            if (categoryHierarchy.isPresent()){
                categoryHierarchyRepository.delete(categoryHierarchy.get());
            }
            return categoryRepository.save(category);
        }
        else{
            Category categoryParent = categoryRepository
                    .findByIdAndActiveAndType(Long.parseLong(categoryDTO.getParentId()), true, type)
                    .orElseThrow(() -> new DataNotFoundException("Cannot find category parent: " + categoryDTO.getParentId()));

            Optional<CategoryHierarchy> categoryHierarchy = Optional.ofNullable(categoryHierarchyRepository.findChildExistInHierarchy(id));
            if (categoryHierarchy.isPresent()){
                if (categoryHierarchy.get().getParent().getId() == categoryParent.getId()){
                    return categoryRepository.save(category);
                }
                categoryHierarchyRepository.delete(categoryHierarchy.get());
            }
            categoryRepository.save(category);
            CategoryHierarchy categoryHierarchyNew = CategoryHierarchy.builder()
                    .parent(categoryParent)
                    .child(category)
                    .build();
            categoryHierarchyRepository.save(categoryHierarchyNew);
            return category;

        }
    }

    @Override
    public void deleteCategory(long id, long[] userId, boolean type) throws DataNotFoundException {
        Category category = categoryRepository.findByIdAndUserId(id, userId[0])
                .orElseThrow(() -> new DataNotFoundException("Cannot find category: " + id));
        // Xóa cha xóa cả con
        List<Category> categories = categoryRepository.findByParent(userId, id, type);
        if (categories.size() > 0){
            for (Category c: categories) {
                c.setActive(false);
                categoryRepository.save(c);
            }
        }
        category.setActive(false);
        categoryRepository.save(category);
    }

    // Lấy các danh mục không phải danh mục con
    @Override
    public List<Category> getAllCategoryIsNotChild(long[] userIds, boolean type){
        return categoryRepository.findCategoriesByUserIsNotChild(userIds, type);
    }

    //Lấy các danh mục con cua 1 danh mục cha
    @Override
    public List<Category> getChildByParent(long[] userId, long parentId, boolean type) {
        return categoryRepository.findByParent(userId, parentId, type);
    }
}
