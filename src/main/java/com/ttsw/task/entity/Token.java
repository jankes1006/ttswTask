package com.ttsw.task.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;
    @OneToOne
    private AppUser appUser;

    public Token(AppUser appUser) {
        this.value = UUID.randomUUID().toString();
        this.appUser = appUser;
    }
}
