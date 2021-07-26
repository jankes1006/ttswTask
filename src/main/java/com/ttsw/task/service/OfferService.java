package com.ttsw.task.service;

import com.ttsw.task.domain.image.ImageAndOfferDTO;
import com.ttsw.task.domain.offer.CreateOfferDTO;
import com.ttsw.task.domain.offer.OfferDTO;
import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Category;
import com.ttsw.task.entity.Image;
import com.ttsw.task.entity.Offer;
import com.ttsw.task.enumVariable.offer.StateOffer;
import com.ttsw.task.exception.category.BadIdCategoryException;
import com.ttsw.task.exception.category.BadNameCategoryException;
import com.ttsw.task.exception.image.BadIdImageException;
import com.ttsw.task.exception.offer.BadEditOfferException;
import com.ttsw.task.exception.offer.BadIdOfferException;
import com.ttsw.task.exception.offer.BadReservedException;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.mapper.offer.OfferMapper;
import com.ttsw.task.repository.AppUserRepository;
import com.ttsw.task.repository.CategoryRepository;
import com.ttsw.task.repository.ImageRepository;
import com.ttsw.task.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class OfferService {
    private final OfferRepository offerRepository;
    private final AppUserRepository appUserRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final OfferMapper offerMapper;
    private final EmailService emailService;

    public OfferDTO create(CreateOfferDTO createOfferDTO, Principal principal) throws BadUsernameException, BadIdCategoryException {
        AppUser owner = appUserRepository.findByUsername(principal.getName()).orElseThrow(BadUsernameException::new);
        Category category = categoryRepository.findById(createOfferDTO.getCategory()).orElseThrow(BadIdCategoryException::new);

        Offer offer = offerMapper.mapToOffer(createOfferDTO);
        offer.setOwner(owner);
        offer.setCategory(category);
        offer.setStateOffer(StateOffer.ACTIVE);
        offer.setCreateDate(LocalDateTime.now());
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


    public OfferDTO updateImage(ImageAndOfferDTO imageAndOfferDTO, Principal principal) throws BadUsernameException, BadEditOfferException, BadIdOfferException, BadIdImageException {
        Offer offerEdited = offerRepository.findById(imageAndOfferDTO.getIdOffer()).orElseThrow(BadIdOfferException::new);
        AppUser appUser = appUserRepository.findByUsername(principal.getName()).orElseThrow(BadUsernameException::new);

        if (!appUser.getUsername().equals(offerEdited.getOwner().getUsername())) {
            throw new BadEditOfferException();
        }

        Image image = imageRepository.findById(imageAndOfferDTO.getIdImage()).orElseThrow(BadIdImageException::new);
        offerEdited.setImage(image);

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

    public List<OfferDTO> getAllAdmin() {
        List<Offer> offers = (List<Offer>) offerRepository.findAll();
        return offerMapper.mapToOffersDTO(offers);
    }

    public OfferDTO setBanOnOffer(Long id, String reason, Principal principal) throws BadIdOfferException, BadUsernameException {
        AppUser admin = appUserRepository.findByUsername(principal.getName()).orElseThrow(BadUsernameException::new);
        Offer offer = offerRepository.findById(id).orElseThrow(BadIdOfferException::new);
        offer.setStateOffer(StateOffer.BANNED);
        offerRepository.save(offer);
        emailService.sendEmailToSellerThatOfferIsBlocked(admin, offer.getOwner(), offer, reason);
        return offerMapper.mapToOfferDTO(offer);
    }

    public OfferDTO takeOffBan(Long id) throws BadIdOfferException {
        Offer offer = offerRepository.findById(id).orElseThrow(BadIdOfferException::new);
        offer.setStateOffer(StateOffer.NO_ACTIVE);
        offerRepository.save(offer);
        emailService.sendEmailToSellerThatOfferIsUnBlock(offer.getOwner(), offer);
        return offerMapper.mapToOfferDTO(offer);
    }

    public Page<OfferDTO> searchTitle(Specification<Offer> spec, Pageable pageable) {
        return offerRepository.findAll(spec, pageable).map(offerMapper::mapToOfferDTO);
    }

    public Page<OfferDTO> searchTitleAdmin(Specification<Offer> spec, Pageable pageable) {
        return offerRepository.findAll(spec, pageable).map(offerMapper::mapToOfferDTO);
    }
}
