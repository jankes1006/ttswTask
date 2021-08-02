package com.ttsw.task.domain.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogReservedDTO {
    private Long id;
    private String usernameClient;
    private String usernameSeller;
    private Long offerId;
    private String titleOffer;
    private LocalDateTime dateTime;
    private String comment;
    private int mark;
}
