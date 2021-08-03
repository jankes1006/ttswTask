package com.ttsw.task.mapper.offer;

import com.ttsw.task.domain.offer.CreateOfferDTO;
import com.ttsw.task.domain.offer.OfferDTO;
import com.ttsw.task.entity.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfferMapper {
    @Mapping(ignore = true, source = "category", target = "category")
    Offer mapToOffer(CreateOfferDTO createOfferDTO);

    @Mapping(source = "owner.username", target = "ownerName")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "category.name", target = "category")
    @Mapping(ignore=true, source = "images", target = "images")
    OfferDTO mapToOfferDTO(Offer offer);

    List<OfferDTO> mapToOffersDTO(List<Offer> offers);

    Iterable<OfferDTO> mapToOfferIterable(Iterable<Offer> offers);

    @Mapping(ignore = true, source = "category", target = "category")
    @Mapping(ignore = true, source = "images", target = "images")
    Offer mapOfferDTOtoOffer(OfferDTO offerDTO);
}
