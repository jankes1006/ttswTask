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
public class LogVisitedOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    private AppUser appUser;

    @OneToOne
    private Offer offer;

    private LocalDateTime localDateTime;

    public LogVisitedOffer(AppUser appUser, Offer offer){
        this.appUser = appUser;
        this.offer = offer;
        this.localDateTime = LocalDateTime.now();
    }
}
