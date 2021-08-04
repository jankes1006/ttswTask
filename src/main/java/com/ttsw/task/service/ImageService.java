package com.ttsw.task.service;

import com.ttsw.task.entity.Image;
import com.ttsw.task.exception.image.BadIdImageException;
import com.ttsw.task.mapper.image.ImageMapper;
import com.ttsw.task.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ImageService {
    final ImageRepository imageRepository;
    final ImageMapper imageMapper;

    public List<Long> uploadImage(List<MultipartFile> files) throws IOException {
        List<Long> imageIndexList = new ArrayList<>();
        for (MultipartFile file : files) {
            Image img = new Image(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            imageIndexList.add(imageRepository.save(img).getId());
        }
        return imageIndexList;
    }

    public Image getImageById(Long id) throws BadIdImageException {
        return imageRepository.findById(id).orElseThrow(BadIdImageException::new);
    }
}
