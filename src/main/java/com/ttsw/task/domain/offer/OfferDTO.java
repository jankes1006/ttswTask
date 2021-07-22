package com.ttsw.task.domain.offer;

import com.ttsw.task.enumVariable.offer.StateOffer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private StateOffer stateOffer;
    private String ownerName;
    private String category;
}
