package com.ttsw.task.repository.log;

import com.ttsw.task.entity.log.LogVisitedOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogVisitedOfferRepository extends JpaRepository<LogVisitedOffer, Long> {
    List<LogVisitedOffer> findByOfferId(Long id);
    Long countByOfferId(Long id);
}
