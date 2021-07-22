package com.ttsw.task.repository;

import com.ttsw.task.entity.Category;
import com.ttsw.task.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    public Page<Offer> findAllByCategory(Category category, Pageable pageable);
}
