package com.ttsw.task.controller;

import com.ttsw.task.domain.offer.CreateOfferDTO;
import com.ttsw.task.domain.offer.OfferDTO;
import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Offer;
import com.ttsw.task.exception.user.BadIdUserException;
import com.ttsw.task.mapper.offer.OfferMapper;
import com.ttsw.task.mapper.offer.OfferMapperResponse;
import com.ttsw.task.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offer")
@RequiredArgsConstructor
public class OfferController {

    private final OfferMapper offerMapper;
    private final OfferMapperResponse offerMapperResponse;
    private final DbService dbService;

    @PostMapping("/create")
    public OfferDTO create(@RequestBody CreateOfferDTO createOfferDTO)throws BadIdUserException{
        AppUser owner = dbService.getUserById(createOfferDTO.getUserId()).orElseThrow(BadIdUserException::new);
        Offer offer = offerMapper.mapToOffer(createOfferDTO);
        owner.getUserOffers().add(offer);
        offer.setOwner(owner);
        offer.setActive(true);
        return offerMapperResponse.mapToOfferDTO(dbService.saveOffer(offer));
    }

    @GetMapping("/getAll")
    public List<OfferDTO> getAll(){
        return offerMapperResponse.mapToOfferDTOList(dbService.getAllOffer());
    }
}
