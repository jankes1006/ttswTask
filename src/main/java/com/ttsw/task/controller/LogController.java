package com.ttsw.task.controller;

import com.ttsw.task.domain.log.LogReservedDTO;
import com.ttsw.task.domain.log.LogReservedUpdateDTO;
import com.ttsw.task.domain.statistic.DonutDTO;
import com.ttsw.task.entity.log.LogReservedOffer;
import com.ttsw.task.exception.log.BadIdOfferToCommentException;
import com.ttsw.task.exception.log.BadNotificationOfferException;
import com.ttsw.task.exception.log.NoAccessToOfferToComment;
import com.ttsw.task.exception.offer.BadIdOfferException;
import com.ttsw.task.exception.user.BadUsernameException;
import com.ttsw.task.service.log.LogService;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThan;
import net.kaczmarzyk.spring.data.jpa.domain.NotEqual;
import net.kaczmarzyk.spring.data.jpa.domain.Null;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

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

    @GetMapping("/getOfferToCommentAndMark")
    public Page<LogReservedDTO> getOfferToComment(
            @And({
                    @Spec(path = "mark", spec = Equal.class, defaultVal = "0"),
                    @Spec(path = "appUser.username", spec = Equal.class, params = "username")
            })
                    Specification<LogReservedOffer> logReservedOfferSpecification, String username, Pageable pageable, Principal principal
    ) throws NoAccessToOfferToComment {
        return logService.getOfferToComment(logReservedOfferSpecification, username, principal, pageable);
    }

    @GetMapping("/getOfferToCommentAndMarkByID")
    public LogReservedDTO getOfferToCommentById(Long id) throws BadIdOfferToCommentException {
        return logService.getOfferToCommentById(id);
    }

    @PutMapping("/updateOfferToComment")
    public LogReservedDTO updateOfferToComment(@RequestBody LogReservedUpdateDTO logReservedUpdateDTO, Principal principal) throws BadUsernameException, BadIdOfferToCommentException {
        return logService.updateOfferToComment(logReservedUpdateDTO, principal);
    }

    @GetMapping("/getNumberOfSoldOfferUser")
    public int getNumberOfSoldOfferUser(
            @Spec(path = "offer.owner.id", spec = Equal.class, params = "id") Specification<LogReservedOffer> logReservedOfferSpecification
    ) {
        return logService.getNumberOfSoldOfferUser(logReservedOfferSpecification);
    }

    @GetMapping("/getAverageMarkUser")
    public double getAverageMarkUser(
            @And({
                    @Spec(path = "offer.owner.id", spec = Equal.class, params = "id"),
                    @Spec(path = "mark", spec = GreaterThan.class, defaultVal = "0")
            }) Specification<LogReservedOffer> logReservedOfferSpecification
    ) {
        return logService.getAverageMarkUser(logReservedOfferSpecification);
    }

    @GetMapping("/getMarksUser")
    public DonutDTO getMarksUser(
            @And({
                    @Spec(path = "offer.owner.id", spec = Equal.class, params = "id"),
                    @Spec(path = "mark", spec = GreaterThan.class, defaultVal = "0")
            }) Specification<LogReservedOffer> logReservedOfferSpecification
    ) {
        return logService.getMarksUser(logReservedOfferSpecification);
    }

    @GetMapping("/getCommentsOfferUser")
    public List<LogReservedDTO> getCommentsOfferUser(
            @And({
                    @Spec(path = "offer.owner.id", spec = Equal.class, params = "id"),
                    @Spec(path = "comment", spec = Null.class, constVal = "false"),
                    @Spec(path = "comment", spec = NotEqual.class, defaultVal = "")
            }) Specification<LogReservedOffer> logReservedOfferSpecification
    ) {
        return logService.getCommentsOfferUser(logReservedOfferSpecification);
    }
}
