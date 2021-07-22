package com.ttsw.task.mapper.category;

import com.ttsw.task.domain.category.CategoryDTO;
import com.ttsw.task.domain.category.CreateCategoryDTO;
import com.ttsw.task.domain.offer.OfferDTO;
import com.ttsw.task.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO mapToCategoryDTO(Category category);

    List<CategoryDTO> mapToCategoryListDTO(List<Category> category);

    Category mapToCategory(OfferDTO offerDTO);

    Category mapCreateCategoryToCategory(CreateCategoryDTO createCategoryDTO);
}
