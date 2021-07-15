package com.ttsw.task.mapper.offer;

import com.ttsw.task.domain.offer.CreateOfferDTO;
import com.ttsw.task.entity.Offer;
import net.bytebuddy.description.ModifierReviewable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OfferMapper {
    Offer mapToOffer(CreateOfferDTO createOfferDTO);
    CreateOfferDTO mapToCreateOfferDTO(Offer offer);
}
