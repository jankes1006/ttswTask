package com.ttsw.task.service;

import com.ttsw.task.domain.category.CategoryDTO;
import com.ttsw.task.domain.category.CreateCategoryDTO;
import com.ttsw.task.entity.Category;
import com.ttsw.task.mapper.category.CategoryMapper;
import com.ttsw.task.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryDTO create(CreateCategoryDTO createCategoryDTO) {
        Category category = categoryMapper.mapCreateCategoryToCategory(createCategoryDTO);
        return categoryMapper.mapToCategoryDTO(categoryRepository.save(category));
    }

    public List<CategoryDTO> getAll() {
        return categoryMapper.mapToCategoryListDTO((List<Category>) categoryRepository.findAll());
    }
}
