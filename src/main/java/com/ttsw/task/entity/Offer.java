package com.ttsw.task.entity;

import com.ttsw.task.enumVariable.offer.StateOffer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private StateOffer stateOffer;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private AppUser owner;
}
