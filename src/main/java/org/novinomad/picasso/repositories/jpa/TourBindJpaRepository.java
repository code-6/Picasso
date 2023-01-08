package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.domain.erm.entities.tour.TourBind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface TourBindJpaRepository extends JpaRepository<TourBind, Long> {

    @Query(nativeQuery = true, value = """
            select tb.* from TOUR_BIND tb
            where (tb.START_DATE <= :newBindEndDate and tb.END_DATE >= :newBindStartDate) and tb.TOUR_ID <> :tourId
            and tb.TOUR_PARTICIPANT_ID = :tourParticipantId  
            """)
    List<TourBind> findOverlappedBinds(Long tourId,
                                       Long tourParticipantId,
                                       LocalDateTime newBindStartDate,
                                       LocalDateTime newBindEndDate);

    @Query("update TourBind set deleted = true where id = :id")
    @Modifying
    int softDeleteById(Long id);

    @Query("update TourBind  set deleted = true where id in(:ids)")
    @Modifying
    int softDeleteById(Collection<Long> ids);
}
