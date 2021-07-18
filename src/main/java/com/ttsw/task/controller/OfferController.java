package com.ttsw.task.controller;

import com.ttsw.task.domain.offer.CreateOfferDTO;
import com.ttsw.task.domain.offer.OfferDTO;
import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Offer;
import com.ttsw.task.enumVariable.offer.StateOffer;
import com.ttsw.task.exception.offer.BadIdOfferException;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.mapper.offer.OfferMapper;
import com.ttsw.task.service.DbService;
import com.ttsw.task.service.EmailService;
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
    private final EmailService emailService;

    @PostMapping("/create")
    public OfferDTO create(@RequestBody CreateOfferDTO createOfferDTO, Principal principal) throws BadUsernameException {
        AppUser owner = dbService.getUserByUsername(principal.getName()).orElseThrow(BadUsernameException::new);
        Offer offer = offerMapper.mapToOffer(createOfferDTO);
        owner.getUserOffers().add(offer);
        offer.setOwner(owner);
        offer.setStateOffer(StateOffer.ACTIVE);
        return offerMapper.mapToOfferDTO(dbService.saveOffer(offer));
    }

    @GetMapping("/getAll")
    public List<OfferDTO> getAll() {
        return offerMapper.mapToOffersDTO(dbService.getAllOffer());
    }

    @GetMapping("/getById")
    public OfferDTO getById(@RequestParam Long id) throws BadIdOfferException {
        return offerMapper.mapToOfferDTO(dbService.getOfferById(id).orElseThrow(BadIdOfferException::new));
    }

    @GetMapping("/reserved")
    public Boolean reserved(@RequestParam Long id, Principal principal) throws BadIdOfferException, BadUsernameException {
        Offer offer = dbService.getOfferById(id).orElseThrow(BadIdOfferException::new);
        if (offer.getStateOffer().equals(StateOffer.ACTIVE)) {
            AppUser client = dbService.getUserByUsername(principal.getName()).orElseThrow(BadUsernameException::new);
            AppUser seller = dbService.getUserByUsername(offer.getOwner().getUsername()).orElseThrow(BadUsernameException::new);

            emailService.sendEmailToSellerBuy(client, seller, offer);
            emailService.sendEmailToClientBuy(client, seller, offer);
            offer.setStateOffer(StateOffer.NO_ACTIVE);
            dbService.saveOffer(offer);
            return true;
        }
        return false;
    }

}
