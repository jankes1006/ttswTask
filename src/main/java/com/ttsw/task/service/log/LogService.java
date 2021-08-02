package com.ttsw.task.service.log;

import com.ttsw.task.domain.log.LogReservedDTO;
import com.ttsw.task.domain.log.LogReservedUpdateDTO;
import com.ttsw.task.domain.statistic.DonutDTO;
import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Offer;
import com.ttsw.task.entity.log.LogLoginToService;
import com.ttsw.task.entity.log.LogNotificationOffer;
import com.ttsw.task.entity.log.LogReservedOffer;
import com.ttsw.task.entity.log.LogVisitedOffer;
import com.ttsw.task.exception.log.BadIdOfferToCommentException;
import com.ttsw.task.exception.log.BadNotificationOfferException;
import com.ttsw.task.exception.log.NoAccessToOfferToComment;
import com.ttsw.task.exception.offer.BadIdOfferException;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.mapper.log.LogReservedMapper;
import com.ttsw.task.repository.AppUserRepository;
import com.ttsw.task.repository.OfferRepository;
import com.ttsw.task.repository.log.LogLoginToServiceRepository;
import com.ttsw.task.repository.log.LogNotificationOfferRepository;
import com.ttsw.task.repository.log.LogReservedOfferRepository;
import com.ttsw.task.repository.log.LogVisitedOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LogService {
    private final LogNotificationOfferRepository logNotificationOfferRepository;
    private final LogVisitedOfferRepository logVisitedOfferRepository;
    private final LogLoginToServiceRepository logLoginToServiceRepository;
    private final LogReservedOfferRepository logReservedOfferRepository;
    private final AppUserRepository appUserRepository;
    private final OfferRepository offerRepository;
    private final LogReservedMapper logReservedMapper;

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

    public void addLogin(AppUser appUser) {
        LogLoginToService logLoginToService = new LogLoginToService(appUser);
        logLoginToServiceRepository.save(logLoginToService);
    }

    public void addReserved(AppUser appUser, Offer offer) {
        LogReservedOffer logReservedOffer = new LogReservedOffer(appUser, offer);
        logReservedOfferRepository.save(logReservedOffer);
    }

    public Page<LogReservedDTO> getOfferToComment(Specification<LogReservedOffer> logReservedOfferSpecification, String username, Principal principal, Pageable pageable) throws NoAccessToOfferToComment {
        if (!username.equals(principal.getName())) {
            throw new NoAccessToOfferToComment();
        }
        Page<LogReservedOffer> logReservedOfferPage = logReservedOfferRepository.findAll(logReservedOfferSpecification, pageable);
        return logReservedOfferPage.map(logReservedMapper::mapToLogReservedDTO);
    }

    public LogReservedDTO getOfferToCommentById(Long id) throws BadIdOfferToCommentException {
        LogReservedOffer logReservedOffer = logReservedOfferRepository.findById(id).orElseThrow(BadIdOfferToCommentException::new);
        return logReservedMapper.mapToLogReservedDTO(logReservedOffer);
    }

    public LogReservedDTO updateOfferToComment(LogReservedUpdateDTO logReservedUpdateDTO, Principal principal) throws BadIdOfferToCommentException, BadUsernameException {
        LogReservedOffer logReservedOffer = logReservedOfferRepository.findById(logReservedUpdateDTO.getId()).orElseThrow(BadIdOfferToCommentException::new);

        if (!logReservedOffer.getAppUser().getUsername().equals(principal.getName())) {
            throw new BadUsernameException();
        }

        logReservedOffer.setComment(logReservedUpdateDTO.getComment());
        logReservedOffer.setMark(logReservedUpdateDTO.getMark());
        logReservedOfferRepository.save(logReservedOffer);
        return logReservedMapper.mapToLogReservedDTO(logReservedOffer);
    }

    public int getNumberOfSoldOfferUser(Specification<LogReservedOffer> logReservedOfferSpecification) {
        List<LogReservedOffer> logReservedOfferList = logReservedOfferRepository.findAll(logReservedOfferSpecification);
        return logReservedOfferList.size();
    }

    public double getAverageMarkUser(Specification<LogReservedOffer> logReservedOfferSpecification) {
        List<LogReservedOffer> logReservedOfferList = logReservedOfferRepository.findAll(logReservedOfferSpecification);
        return logReservedOfferList.stream().mapToDouble(d -> d.getMark()).average().orElse(0.0);
    }

    public DonutDTO getMarksUser(Specification<LogReservedOffer> logReservedOfferSpecification) {
        List<LogReservedOffer> logReservedOfferList = logReservedOfferRepository.findAll(logReservedOfferSpecification);
        List<Integer> dataList = new ArrayList<>();
        List<String> labelList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            dataList.add(0);
            labelList.add(String.valueOf(i + 1));
        }

        logReservedOfferList.forEach(s -> dataList.set(s.getMark() - 1, dataList.get(s.getMark() - 1) + 1));

        return new DonutDTO(labelList, dataList);
    }

    public List<LogReservedDTO> getCommentsOfferUser(Specification<LogReservedOffer> logReservedOfferSpecification) {
        List<LogReservedOffer> logReservedOfferList = logReservedOfferRepository.findAll(logReservedOfferSpecification);
        return logReservedMapper.mapToLogReservedDTOList(logReservedOfferList);
    }
}