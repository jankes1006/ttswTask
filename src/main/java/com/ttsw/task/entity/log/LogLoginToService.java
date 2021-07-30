package com.ttsw.task.entity.log;

import com.ttsw.task.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogLoginToService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private AppUser appUser;
    private LocalDateTime localDateTime;

    public LogLoginToService(AppUser appUser) {
        this.appUser = appUser;
        this.localDateTime = LocalDateTime.now();
    }
}
