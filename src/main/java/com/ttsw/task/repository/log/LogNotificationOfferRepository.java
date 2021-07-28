package com.ttsw.task.repository.log;

import com.ttsw.task.entity.log.LogNotificationOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LogNotificationOfferRepository extends JpaRepository<LogNotificationOffer, Long>, JpaSpecificationExecutor<LogNotificationOffer> {
    List<LogNotificationOffer> findByOfferId(Long id);
}
