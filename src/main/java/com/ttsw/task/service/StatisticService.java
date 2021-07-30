package com.ttsw.task.service;

import com.ttsw.task.domain.statistic.LinearDTO;
import com.ttsw.task.entity.log.LogVisitedOffer;
import com.ttsw.task.repository.log.LogVisitedOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatisticService {
    final LogVisitedOfferRepository logVisitedOfferRepository;

    private List<String> generatedHoursLabel() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String temp = String.valueOf(i);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            result.add(temp);
        }
        return result;
    }

    public LinearDTO getViewsOfferOnHour(Long id) {
        List<String> labels = generatedHoursLabel();
        List<Long> data = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            data.add(0L);
        }

        List<LogVisitedOffer> logVisitedOffers = logVisitedOfferRepository.findByOfferId(id);

        logVisitedOffers.forEach(visitedOffer -> {
            int hour = visitedOffer.getLocalDateTime().getHour();
            data.set(hour, data.get(hour) + 1);
        });

        return new LinearDTO(labels, data);
    }
}
