package com.ttsw.task.domain.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageAndOfferDTO {
    private Long idOffer;
    private Long idImage;
}
