package com.ttsw.task.entity;

import com.ttsw.task.enumVariable.offer.StateOffer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private StateOffer stateOffer;
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private AppUser owner;

    @ManyToOne
    @JoinColumn(name = "CATEGORY")
    private Category category;

    @OneToMany(
            mappedBy = "offer",
            cascade = CascadeType.ALL
    )
    private List<Image> images;

//    @Formula(value="(SELECT count(*) FROM LogNotificationOffer lno WHERE lno.offer = this..")
//    private int notification;
}
