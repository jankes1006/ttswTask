package com.ttsw.task.domain.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DonutDTO {
    List<String> labels;
    List<Integer> data;
}
