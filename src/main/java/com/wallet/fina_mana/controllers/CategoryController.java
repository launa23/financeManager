package com.wallet.fina_mana.controllers;

import com.wallet.fina_mana.dtos.CategoryDTO;
import com.wallet.fina_mana.models.Category;
import com.wallet.fina_mana.responses.CategoryResponse;
import com.wallet.fina_mana.services.CategoryService;
import com.wallet.fina_mana.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;

    @PostMapping("/create/{type}")
    public ResponseEntity<?> createCategory(@PathVariable("type") String type,
                                            @Valid @RequestBody CategoryDTO categoryDTO,
                                            HttpServletRequest request,
                                            BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMess = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMess);
            }
            long userId = userService.getCurrent(request).getId();
            long[] userIds = new long[]{0, userId};
            boolean typeBoolean = type.equals("income");
            Category category = categoryService.createCategory(userIds, categoryDTO, typeBoolean);
            CategoryResponse categoryResponse = CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .icon(category.getIcon())
                    .categoryOf(category.getUser() != null ? "User" : "System")
                    .build();
            return ResponseEntity.ok(categoryResponse);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/{type}")
    public ResponseEntity<?> getCategoryByType(@PathVariable("type") String type,
                                               HttpServletRequest request){
        try {
            boolean typeBoolean = type.equals("income");
            long userId = userService.getCurrent(request).getId();
            long[] userIds = new long[]{0, userId};
            List<CategoryResponse> categoryResponses = categoryService.getAllCategoryByType(userIds, typeBoolean);
            return ResponseEntity.ok(categoryResponses);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/parent/{type}")
    public ResponseEntity<?> getCategoryParents(@PathVariable("type") String type,
                                                HttpServletRequest request){
        try {
            boolean typeBoolean = type.equals("income");
            long userId = userService.getCurrent(request).getId();
            long[] userIds = new long[]{0, userId};        // 1 thay bằng current
            List<CategoryResponse> categoryResponses = categoryService.getAllCategoryIsNotChild(userIds, typeBoolean)
                    .stream().map(category -> CategoryResponse.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .icon(category.getIcon())
                            .categoryOf(category.getUser() != null ? "User" : "System")
                            .build()).toList();
            return ResponseEntity.ok(categoryResponses);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/update/{type}/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") long id,
                                            @PathVariable("type") String type,
                                            @Valid @RequestBody CategoryDTO categoryDTO,
                                            HttpServletRequest request,
                                            BindingResult result){
        try {
            if (result.hasErrors()){
                List<String> errorMess = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMess);
            }
            boolean typeBoolean = type.equals("income");
            long userId = userService.getCurrent(request).getId();
            long[] userIds = new long[]{0, userId};
            Category category = categoryService.updateCategoryById(userIds, id, categoryDTO, typeBoolean);
            CategoryResponse categoryResponse = CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .icon(category.getIcon())
                    .categoryOf(category.getUser() != null ? "User" : "System")
                    .build();
            return ResponseEntity.ok(categoryResponse);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @PutMapping("/delete/{type}/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") long id,
                                            @PathVariable("type") String type,
                                            HttpServletRequest request){
        try {
            long userId = userService.getCurrent(request).getId();
            long[] userIds = new long[]{userId};        // 1 thay bằng current
            boolean typeBoolean = type.equals("income");
            categoryService.deleteCategory(id, userIds, typeBoolean);
            return ResponseEntity.ok("Xóa category thành công: " + id);

        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}


