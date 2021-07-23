package com.ttsw.task.controller;

import com.ttsw.task.domain.offer.BanOfferDTO;
import com.ttsw.task.domain.offer.CreateOfferDTO;
import com.ttsw.task.domain.offer.OfferDTO;
import com.ttsw.task.entity.Offer;
import com.ttsw.task.exception.category.BadIdCategoryException;
import com.ttsw.task.exception.offer.BadEditOfferException;
import com.ttsw.task.exception.offer.BadIdOfferException;
import com.ttsw.task.exception.offer.BadReservedException;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.mapper.offer.OfferMapper;
import com.ttsw.task.repository.OfferRepository;
import com.ttsw.task.service.OfferService;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
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
    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    @PostMapping("/create")
    public OfferDTO create(@RequestBody CreateOfferDTO createOfferDTO, Principal principal) throws BadUsernameException, BadIdCategoryException {
        return offerService.create(createOfferDTO, principal);
    }

    @PutMapping("/update")
    public OfferDTO update(@RequestBody OfferDTO offerDTO, Principal principal) throws BadUsernameException, BadEditOfferException {
        return offerService.update(offerDTO, principal);
    }

    @PutMapping("/changeActivityUser")
    public OfferDTO updateActivityUser(@RequestParam Long id, Principal principal) throws BadIdOfferException, BadUsernameException {
        return offerService.updateActivityUser(id, principal);
    }

    @GetMapping("/getAllUser")
    public List<OfferDTO> getAllUser(Principal principal) throws BadUsernameException {
        return offerService.getAllUser(principal);
    }

    @GetMapping("/getAll")
    public List<OfferDTO> getAll() {
        return offerService.getAll();
    }

    @GetMapping("/getAllAdmin")
    public List<OfferDTO> getAllAdmin() {
        return offerService.getAllAdmin();
    }

    @PutMapping("/setBan")
    public OfferDTO setBanOnOffer(@RequestBody BanOfferDTO banOfferDTO, Principal principal) throws BadIdOfferException, BadUsernameException {
        return offerService.setBanOnOffer(banOfferDTO.getId(), banOfferDTO.getReason(), principal);
    }

    @PutMapping("/takeOffBan")
    public OfferDTO takeOffBanOffer(@RequestBody BanOfferDTO banOfferDTO) throws BadIdOfferException {
        return offerService.takeOffBan(banOfferDTO.getId());
    }

    @GetMapping("/getById")
    public OfferDTO getById(@RequestParam Long id) throws BadIdOfferException {
        return offerService.getById(id);
    }

    @GetMapping("/reserved")
    public Boolean reserved(@RequestParam Long id, Principal principal) throws BadIdOfferException, BadUsernameException, BadReservedException {
        return offerService.reserved(id, principal);
    }

    @GetMapping(value = "searchTitle", params = {"title", "category", "page", "size", "username"})
    public Page<OfferDTO> searchTitle(
            @Join(path = "category", alias = "c")
            @Join(path = "owner", alias = "o")
            @And({
                    @Spec(path = "c.name", params = "category", spec = Like.class),
                    @Spec(path = "title", params = "title", spec = Like.class),
                    @Spec(path = "o.username", params = "username", spec = Like.class),
                    @Spec(path = "stateOffer", spec = Equal.class, defaultVal = "ACTIVE")
            })
                    Specification<Offer> spec, Pageable pageable) {
        return offerService.searchTitle(spec, pageable);
    }

    @GetMapping(value = "searchTitleAdmin", params = {"title", "category", "page", "size", "username"})
    public Page<OfferDTO> searchTitleAdmin(
            @Join(path = "category", alias = "c")
            @Join(path = "owner", alias = "o")
            @And({
                    @Spec(path = "c.name", params = "category", spec = Like.class),
                    @Spec(path = "title", params = "title", spec = Like.class),
                    @Spec(path = "o.username", params = "username", spec = Like.class)
            })
                    Specification<Offer> spec, Pageable pageable) {
        return offerService.searchTitleAdmin(spec, pageable);
    }
}
