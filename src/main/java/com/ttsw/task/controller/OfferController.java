package com.ttsw.task.controller;

import com.ttsw.task.domain.offer.BanOfferDTO;
import com.ttsw.task.domain.offer.CreateOfferDTO;
import com.ttsw.task.domain.offer.OfferDTO;
import com.ttsw.task.exception.category.BadIdCategoryException;
import com.ttsw.task.exception.category.BadNameCategoryException;
import com.ttsw.task.exception.offer.BadEditOfferException;
import com.ttsw.task.exception.offer.BadIdOfferException;
import com.ttsw.task.exception.offer.BadReservedException;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/offer")
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;

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

    @GetMapping("/paginationALlOffer")
    public List<OfferDTO> getPaginationOffers(@RequestParam int page, int size) {
        return offerService.getPaginationOffers(page, size);
    }

    @GetMapping("/getSizeActive")
    public int getSizeActiveOffers(@RequestParam String category) throws BadNameCategoryException {
        return offerService.sizeActiveOffer(category);
    }

    @GetMapping("/paginationOfferWhereCategory")
    public List<OfferDTO> getPaginationOffersWhereCategory(@RequestParam int page, int sizePage, String category) throws BadNameCategoryException {
        return offerService.getPaginationOffersWhereCategory(page, sizePage, category);
    }
}
