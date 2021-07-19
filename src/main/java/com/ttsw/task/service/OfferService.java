package com.ttsw.task.service;

import com.ttsw.task.domain.offer.CreateOfferDTO;
import com.ttsw.task.domain.offer.OfferDTO;
import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Offer;
import com.ttsw.task.enumVariable.offer.StateOffer;
import com.ttsw.task.exception.offer.BadEditOfferException;
import com.ttsw.task.exception.offer.BadIdOfferException;
import com.ttsw.task.exception.offer.BadReservedException;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.mapper.offer.OfferMapper;
import com.ttsw.task.repository.AppUserRepository;
import com.ttsw.task.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class OfferService {
    private final OfferRepository offerRepository;
    private final AppUserRepository appUserRepository;
    private final OfferMapper offerMapper;
    private final EmailService emailService;

    public OfferDTO create(CreateOfferDTO createOfferDTO, Principal principal) throws BadUsernameException {
        AppUser owner = appUserRepository.findByUsername(principal.getName()).orElseThrow(BadUsernameException::new);
        Offer offer = offerMapper.mapToOffer(createOfferDTO);
        owner.getUserOffers().add(offer);
        offer.setOwner(owner);
        offer.setStateOffer(StateOffer.ACTIVE);
        return offerMapper.mapToOfferDTO(offerRepository.save(offer));
    }

    public OfferDTO update(OfferDTO offerDTO, Principal principal) throws BadUsernameException, BadEditOfferException {
        Offer offerEdited = offerMapper.mapOfferDTOtoOffer(offerDTO);
        Offer offerOriginal = offerRepository.findById(offerEdited.getId()).orElseThrow(BadEditOfferException::new);

        AppUser appUser = appUserRepository.findByUsername(principal.getName()).orElseThrow(BadUsernameException::new);

        if (!appUser.getUsername().equals(offerDTO.getOwnerName())) {
            throw new BadEditOfferException();
        }

        offerEdited.setStateOffer(offerOriginal.getStateOffer());
        offerEdited.setOwner(appUser);

        return offerMapper.mapToOfferDTO(offerRepository.save(offerEdited));
    }

    public OfferDTO updateActivityUser(Long id, Principal principal) throws BadIdOfferException, BadUsernameException {
        Offer offer = offerRepository.findById(id).orElseThrow(BadIdOfferException::new);

        if (!offer.getOwner().getUsername().equals(principal.getName())) {
            throw new BadUsernameException();
        }

        switch (offer.getStateOffer()) {
            case ACTIVE:
                offer.setStateOffer(StateOffer.NO_ACTIVE);
                break;

            case NO_ACTIVE:
                offer.setStateOffer(StateOffer.ACTIVE);
                break;
        }

        return offerMapper.mapToOfferDTO(offerRepository.save(offer));
    }

    public List<OfferDTO> getAllUser(Principal principal) throws BadUsernameException {
        AppUser appUser = appUserRepository.findByUsername(principal.getName()).orElseThrow(BadUsernameException::new);
        return offerMapper.mapToOffersDTO(appUser.getUserOffers());
    }

    public List<OfferDTO> getAll() {
        List<Offer> offers = (List<Offer>) offerRepository.findAll();
        return offerMapper.mapToOffersDTO(
                offers.stream()
                        .filter(s -> s.getStateOffer().equals(StateOffer.ACTIVE))
                        .collect(Collectors.toList())
        );
    }

    public OfferDTO getById(Long id) throws BadIdOfferException {
        return offerMapper.mapToOfferDTO(offerRepository.findById(id).orElseThrow(BadIdOfferException::new));
    }

    public Boolean reserved(Long id, Principal principal) throws BadIdOfferException, BadUsernameException, BadReservedException {
        Offer offer = offerRepository.findById(id).orElseThrow(BadIdOfferException::new);
        if (offer.getOwner().getUsername().equals(principal.getName())) {
            throw new BadReservedException();
        }

        if (offer.getStateOffer().equals(StateOffer.ACTIVE)) {
            AppUser client = appUserRepository.findByUsername(principal.getName()).orElseThrow(BadUsernameException::new);
            AppUser seller = appUserRepository.findByUsername(offer.getOwner().getUsername()).orElseThrow(BadUsernameException::new);

            emailService.sendEmailToSellerBuy(client, seller, offer);
            emailService.sendEmailToClientBuy(client, seller, offer);
            offer.setStateOffer(StateOffer.NO_ACTIVE);
            offerRepository.save(offer);
            return true;
        }
        return false;
    }
}
