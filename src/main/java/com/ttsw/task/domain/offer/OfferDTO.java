package com.ttsw.task.domain.offer;

import com.ttsw.task.enumVariable.offer.StateOffer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private StateOffer stateOffer;
    private Long ownerId;
    private String ownerName;
    private String category;
    private LocalDateTime createDate;
    private Long image;
}
