package com.ttsw.task.entity.log;

import com.ttsw.task.entity.AppUser;
import com.ttsw.task.entity.Offer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogNotificationOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    LocalDateTime localDateTime;

    @OneToOne
    AppUser appUser;
    @OneToOne
    Offer offer;

    public LogNotificationOffer(AppUser appUser, Offer offer){
        this.appUser = appUser;
        this.offer = offer;
        this.localDateTime = LocalDateTime.now();
    }
}
