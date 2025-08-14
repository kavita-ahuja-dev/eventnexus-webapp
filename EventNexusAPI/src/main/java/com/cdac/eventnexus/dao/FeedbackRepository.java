package com.cdac.eventnexus.dao;

import com.cdac.eventnexus.entities.Feedback;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByIsActiveTrue();
    boolean existsByUser_IdAndEvent_Id(Long userId, Long eventId);


}
