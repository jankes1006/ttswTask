package com.ttsw.task.controller;

import com.ttsw.task.domain.offer.CreateOfferDTO;
import com.ttsw.task.domain.offer.OfferDTO;
import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Offer;
import com.ttsw.task.exception.offer.BadIdOfferException;
import com.ttsw.task.exception.user.BadIdUserException;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.mapper.offer.OfferMapper;
import com.ttsw.task.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/offer")
@RequiredArgsConstructor
public class OfferController {
    private final OfferMapper offerMapper;
    private final DbService dbService;

    @PostMapping("/create")
    public OfferDTO create(@RequestBody CreateOfferDTO createOfferDTO, Principal principal)throws BadUsernameException{
        AppUser owner = dbService.getUserByUsername(principal.getName()).orElseThrow(BadUsernameException::new);
        Offer offer = offerMapper.mapToOffer(createOfferDTO);
        owner.getUserOffers().add(offer);
        offer.setOwner(owner);
        offer.setActive(true);
        return offerMapper.mapToOfferDTO(dbService.saveOffer(offer));
    }

    @GetMapping("/getAll")
    public List<OfferDTO> getAll(){
        return offerMapper.mapToOffersDTO(dbService.getAllOffer());
    }

    @GetMapping("/getById")
    public OfferDTO getById(@RequestParam Long id)throws BadIdOfferException{
        return offerMapper.mapToOfferDTO(dbService.getOfferById(id).orElseThrow(BadIdOfferException::new));
    }
}
