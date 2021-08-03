package com.ttsw.task.mapper.image;

import com.ttsw.task.domain.image.ImageDTO;
import com.ttsw.task.entity.Image;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageDTO mapToImageDTO(Image image);
    List<ImageDTO> mapToImageListDTO(List<Image> imageList);
}
