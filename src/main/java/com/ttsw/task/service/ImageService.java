package com.ttsw.task.service;

import com.ttsw.task.domain.image.ImageDTO;
import com.ttsw.task.entity.Image;
import com.ttsw.task.exception.image.BadIdImageException;
import com.ttsw.task.mapper.image.ImageMapper;
import com.ttsw.task.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@AllArgsConstructor
public class ImageService {
    final ImageRepository imageRepository;
    final ImageMapper imageMapper;

    public ImageDTO uploadImage(MultipartFile file) throws IOException {
        Image img = new Image(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        return imageMapper.mapToImageDTO(imageRepository.save(img));
    }

    public Image getImageById(Long id)throws BadIdImageException {
        return imageRepository.findById(id).orElseThrow(BadIdImageException::new);
    }
}
