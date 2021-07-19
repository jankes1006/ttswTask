package com.ttsw.task.mapper.offer;

import com.ttsw.task.domain.offer.CreateOfferDTO;
import com.ttsw.task.domain.offer.OfferDTO;
import com.ttsw.task.entity.Offer;
import net.bytebuddy.description.ModifierReviewable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfferMapper {
    Offer mapToOffer(CreateOfferDTO createOfferDTO);

    CreateOfferDTO mapToCreateOfferDTO(Offer offer);

    @Mapping(source = "owner.username", target = "ownerName")
    OfferDTO mapToOfferDTO(Offer offer);

    List<OfferDTO> mapToOffersDTO(List<Offer> offers);

    Offer mapOfferDTOtoOffer(OfferDTO offerDTO);
}
