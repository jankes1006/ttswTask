package com.ttsw.task.domain.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageAndOfferDTO {
    private Long idOffer;
    private List<Long> idImage;
}
