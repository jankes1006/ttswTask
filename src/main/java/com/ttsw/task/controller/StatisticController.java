package com.ttsw.task.controller;

import com.ttsw.task.domain.statistic.LinearDTO;
import com.ttsw.task.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistic")
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/viewsOfferOnHour")
    LinearDTO getViewsOfferOnHour(Long id) {
        return statisticService.getViewsOfferOnHour(id);
    }
}
