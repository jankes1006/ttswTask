package com.ttsw.task.controller;

import com.ttsw.task.domain.image.ImageDTO;
import com.ttsw.task.domain.offer.OfferDTO;
import com.ttsw.task.entity.Image;
import com.ttsw.task.exception.category.BadIdCategoryException;
import com.ttsw.task.exception.image.BadIdImageException;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.service.ImageService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RequestMapping("image")
@RequiredArgsConstructor
@RestController
public class ImageController {
    final ImageService imageService;

    @PostMapping
    public ImageDTO create(@RequestParam("imageFile") MultipartFile multipartFile) throws IOException {
        return imageService.uploadImage(multipartFile);
    }

    @GetMapping
    public Image getImageById(Long id) throws BadIdImageException {
        return imageService.getImageById(id);
    }
}
