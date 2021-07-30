package com.ttsw.task.service.log;

import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Offer;
import com.ttsw.task.entity.log.LogLoginToService;
import com.ttsw.task.entity.log.LogNotificationOffer;
import com.ttsw.task.entity.log.LogVisitedOffer;
import com.ttsw.task.exception.log.BadNotificationOfferException;
import com.ttsw.task.exception.offer.BadIdOfferException;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.repository.AppUserRepository;
import com.ttsw.task.repository.OfferRepository;
import com.ttsw.task.repository.log.LogLoginToServiceRepository;
import com.ttsw.task.repository.log.LogNotificationOfferRepository;
import com.ttsw.task.repository.log.LogVisitedOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LogService {
    private final LogNotificationOfferRepository logNotificationOfferRepository;
    private final LogVisitedOfferRepository logVisitedOfferRepository;
    private final LogLoginToServiceRepository logLoginToServiceRepository;
    private final AppUserRepository appUserRepository;
    private final OfferRepository offerRepository;

    public void notificationOffer(Long idOffer, Principal principal) throws BadUsernameException, BadIdOfferException, BadNotificationOfferException {
        AppUser appUser = appUserRepository.findByUsername(principal.getName()).orElseThrow(BadUsernameException::new);
        Offer offer = offerRepository.findById(idOffer).orElseThrow(BadIdOfferException::new);

        if (offer.getOwner().getUsername().equals(appUser.getUsername())) {
            throw new BadNotificationOfferException();
        }

        LogNotificationOffer logNotificationOffer = new LogNotificationOffer(appUser, offer);
        logNotificationOfferRepository.save(logNotificationOffer);
    }

    public boolean isUserNotificationOffer(Long id, Principal principal) {
        List<LogNotificationOffer> logNotificationOffers = logNotificationOfferRepository.findByOfferId(id);

        long numberNotification = logNotificationOffers.stream().filter(log -> log.getAppUser().getUsername().equals(principal.getName())).count();

        return numberNotification > 0;
    }

    public HashMap<Long, Long> getNumbersNotification() {
        List<LogNotificationOffer> logNotificationOffers = logNotificationOfferRepository.findAll();
        HashMap<Long, Long> notificationMap = new HashMap<>();

        logNotificationOffers.forEach(e -> {
            if (!notificationMap.containsKey(e.getOffer().getId())) {
                notificationMap.put(e.getOffer().getId(), 1L);
            } else {
                notificationMap.replace(e.getOffer().getId(), notificationMap.get(e.getOffer().getId()) + 1);
            }
        });
        
        return notificationMap;
    }

    public int numberOfNotificationOfferId(Long id) {
        List<LogNotificationOffer> logNotificationOffers = logNotificationOfferRepository.findByOfferId(id);
        return logNotificationOffers.size();
    }

    public void addVisited(Offer offer, Principal principal) throws BadUsernameException {
        AppUser appUser = appUserRepository.findByUsername(principal.getName()).orElseThrow(BadUsernameException::new);

        if (!appUser.getUsername().equals(offer.getOwner().getUsername())) {
            LogVisitedOffer logVisitedOffer = new LogVisitedOffer(appUser, offer);
            logVisitedOfferRepository.save(logVisitedOffer);
        }
    }

    public int numberOfVisitedOffer(Long id) {
        List<LogVisitedOffer> logVisitedOffers = logVisitedOfferRepository.findByOfferId(id);
        return logVisitedOffers.size();
    }

    public void addLogin(AppUser appUser){
        LogLoginToService logLoginToService = new LogLoginToService(appUser);
        logLoginToServiceRepository.save(logLoginToService);
    }
}
