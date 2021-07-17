package com.ttsw.task.domain.offer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOfferDTO {
    private String title;
    private String description;
    private BigDecimal price;
}
