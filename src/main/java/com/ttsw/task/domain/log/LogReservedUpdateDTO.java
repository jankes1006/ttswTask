package com.ttsw.task.domain.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogReservedUpdateDTO {
    Long id;
    String comment;
    int mark;
}
