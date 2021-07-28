package com.ttsw.task.controller;

import com.ttsw.task.exception.log.BadNotificationOfferException;
import com.ttsw.task.exception.offer.BadIdOfferException;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.service.log.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;

@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;

    @GetMapping("/notificationOffer")
    public void notificationOffer(Long id, Principal principal) throws BadIdOfferException, BadNotificationOfferException, BadUsernameException {
        logService.notificationOffer(id, principal);
    }

    @GetMapping("/isUserNotificationOffer")
    public boolean isUserNotificationOffer(Long id, Principal principal) {
        return logService.isUserNotificationOffer(id, principal);
    }

    @GetMapping("/getNumbersNoticifation")
    public HashMap<Long, Long> isUserNotificationOffer() {
        return logService.getNumbersNotification();
    }

    @GetMapping("/numberOfNotificationOfferId")
    public int numberOfNotificationOfferId(Long id) {
        return logService.numberOfNotificationOfferId(id);
    }

    @GetMapping("/numberOfVisitedOffer")
    public int numberOfVisitedOffer(Long id) {
        return logService.numberOfVisitedOffer(id);
    }
}
