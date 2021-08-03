package com.ttsw.task.controller;

import com.ttsw.task.domain.image.ImageAndOfferDTO;
import com.ttsw.task.domain.image.ImageDTO;
import com.ttsw.task.domain.offer.BanOfferDTO;
import com.ttsw.task.domain.offer.CreateOfferDTO;
import com.ttsw.task.domain.offer.OfferDTO;
import com.ttsw.task.entity.Image;
import com.ttsw.task.entity.Offer;
import com.ttsw.task.exception.category.BadIdCategoryException;
import com.ttsw.task.exception.image.BadIdImageException;
import com.ttsw.task.exception.offer.BadEditOfferException;
import com.ttsw.task.exception.offer.BadIdOfferException;
import com.ttsw.task.exception.offer.BadReservedException;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.service.OfferService;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/offer")
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;

    @PostMapping
    public OfferDTO create(@RequestBody CreateOfferDTO createOfferDTO, Principal principal) throws BadUsernameException, BadIdCategoryException {
        System.out.println(createOfferDTO);
        return offerService.create(createOfferDTO, principal);
    }

    @PutMapping
    public OfferDTO update(@RequestBody OfferDTO offerDTO, Principal principal) throws BadUsernameException, BadEditOfferException {
        return offerService.update(offerDTO, principal);
    }

    @PutMapping("/setImage")
    public OfferDTO updateImage(@RequestBody ImageAndOfferDTO imageAndOfferDTO, Principal principal) throws BadUsernameException, BadEditOfferException, BadIdOfferException, BadIdImageException {
        return offerService.updateImage(imageAndOfferDTO, principal);
    }

    @PutMapping("/changeActivityUser")
    public OfferDTO updateActivityUser(@RequestParam Long id, Principal principal) throws BadIdOfferException, BadUsernameException {
        return offerService.updateActivityUser(id, principal);
    }

    @GetMapping("/getAllUser")
    public List<OfferDTO> getAllUser(Principal principal) throws BadUsernameException {
        return offerService.getAllUser(principal);
    }

    @PutMapping("/setBan")
    public OfferDTO setBanOnOffer(@RequestBody BanOfferDTO banOfferDTO, Principal principal) throws BadIdOfferException, BadUsernameException {
        return offerService.setBanOnOffer(banOfferDTO.getId(), banOfferDTO.getReason(), principal);
    }

    @PutMapping("/takeOffBan")
    public OfferDTO takeOffBanOffer(@RequestBody BanOfferDTO banOfferDTO) throws BadIdOfferException {
        return offerService.takeOffBan(banOfferDTO.getId());
    }

    @GetMapping
    public OfferDTO getById(@RequestParam Long id, Principal principal) throws BadIdOfferException, BadUsernameException {
        return offerService.getById(id, principal);
    }

    @GetMapping("/reserved")
    public Boolean reserved(@RequestParam Long id, Principal principal) throws BadIdOfferException, BadUsernameException, BadReservedException {
        return offerService.reserved(id, principal);
    }

    @GetMapping(value = "searchTitle")
    public Page<OfferDTO> searchTitle(
            @And({
                    @Spec(path = "category.name", params = "category", spec = Like.class),
                    @Spec(path = "title", params = "title", spec = Like.class),
                    @Spec(path = "owner.username", params = "username", spec = Like.class),
                    @Spec(path = "stateOffer", spec = Equal.class, defaultVal = "ACTIVE")
            })
                    Specification<Offer> spec, Pageable pageable) {
        return offerService.searchTitle(spec, pageable);
    }

    @GetMapping(value = "searchTitleAdmin")
    public Page<OfferDTO> searchTitleAdmin(
            @And({
                    @Spec(path = "category.name", params = "category", spec = Like.class),
                    @Spec(path = "title", params = "title", spec = Like.class),
                    @Spec(path = "owner.username", params = "username", spec = Like.class)
            })
                    Specification<Offer> spec, Pageable pageable) {
        return offerService.searchTitleAdmin(spec, pageable);
    }

    @GetMapping("/getImages")
    public List<ImageDTO> getAllOfferImages(Long id) throws BadIdOfferException {
        return offerService.getALlOfferImages(id);
    }
}
