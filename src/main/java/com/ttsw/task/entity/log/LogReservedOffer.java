package com.ttsw.task.entity.log;

import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Offer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LogReservedOffer {
    @OneToOne
    AppUser appUser;
    @OneToOne
    Offer offer;
    LocalDateTime dateTime;
    int mark;
    String comment;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public LogReservedOffer(AppUser appUser, Offer offer) {
        this.offer = offer;
        this.appUser = appUser;
        this.dateTime = LocalDateTime.now();
    }
}
