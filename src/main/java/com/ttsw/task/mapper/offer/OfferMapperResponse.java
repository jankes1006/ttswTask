package com.ttsw.task.mapper.offer;

import com.ttsw.task.domain.offer.CreateOfferDTO;
import com.ttsw.task.domain.offer.OfferDTO;
import com.ttsw.task.entity.Offer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OfferMapperResponse {
    public OfferDTO mapToOfferDTO(Offer offer){
        return new OfferDTO(offer.getId(), offer.getTitle(), offer.getDescription(), offer.getPrice(),offer.getOwner().getUsername());
    }

    public List<OfferDTO> mapToOfferDTOList(List<Offer> offers){
        return offers.stream().map(s->mapToOfferDTO(s)).collect(Collectors.toList());
    }
}
