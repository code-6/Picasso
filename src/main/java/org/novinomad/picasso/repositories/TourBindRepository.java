package org.novinomad.picasso.repositories;

import org.novinomad.picasso.erm.entities.TourBind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface TourBindRepository extends JpaRepository<TourBind, Long> {

    List<TourBind> findOverlappedBinds(Long tourId, Long tourParticipantId, LocalDateTime newBindStartDate, LocalDateTime newBindEndDate);

    List<TourBind> findByFilter(LocalDateTime startDate, LocalDateTime endDate, List<Long> tourIds, List<Long> tourParticipantIds);
}
