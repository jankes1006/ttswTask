package com.ttsw.task.repository.log;

import com.ttsw.task.entity.log.LogReservedOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LogReservedOfferRepository extends JpaRepository<LogReservedOffer, Long>, JpaSpecificationExecutor<LogReservedOffer> {
}
