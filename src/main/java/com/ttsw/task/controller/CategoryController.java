package com.ttsw.task.controller;

import com.ttsw.task.domain.category.CategoryDTO;
import com.ttsw.task.domain.category.CreateCategoryDTO;
import com.ttsw.task.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public CategoryDTO create(@RequestBody CreateCategoryDTO createCategoryDTO) {
        return categoryService.create(createCategoryDTO);
    }

    @GetMapping("/getAll")
    public List<CategoryDTO> getAll() {
        return categoryService.getAll();
    }
}
