package com.ttsw.task.controller;

import com.ttsw.task.entity.Image;
import com.ttsw.task.exception.image.BadIdImageException;
import com.ttsw.task.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("image")
@RequiredArgsConstructor
@RestController
public class ImageController {
    final ImageService imageService;

    @PostMapping
    public List<Long> create(@RequestParam("imageFile") List<MultipartFile> multipartFiles) throws IOException {
        return imageService.uploadImage(multipartFiles);
    }

    @GetMapping
    public Image getImageById(Long id) throws BadIdImageException {
        return imageService.getImageById(id);
    }
}
