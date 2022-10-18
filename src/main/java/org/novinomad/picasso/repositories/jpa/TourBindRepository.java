package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.entities.domain.impl.TourBind;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

@Qualifier("tourBindRepository")
public interface TourBindRepository extends JpaRepository<TourBind, Long> {

    @Query(nativeQuery = true, value = """
            select tb.* from TOUR_BIND tb
            where (tb.START_DATE <= :newBindEndDate and tb.END_DATE >= :newBindStartDate) and tb.TOUR_ID <> :tourId
            and tb.TOUR_PARTICIPANT_ID = :tourParticipantId  
            """)
    List<TourBind> findOverlappedBinds(Long tourId, Long tourParticipantId, LocalDateTime newBindStartDate, LocalDateTime newBindEndDate);

    @Query(nativeQuery = true, value = """
            select tb.id, tb.tour_id, tb.tour_participant_id, tb.start_date, tb.end_date
            from TOUR_BIND tb
            join TOUR t on tb.tour_id = t.id
            left join TOUR_PARTICIPANT e on tb.TOUR_PARTICIPANT_ID = e.ID
            where (:startDate is null or t.start_date <= :startDate and t.end_date >= :startDate )
            or ( (:startDate is null or t.start_date >= :startDate) and (:endDate is null or t.end_date <= :endDate) ) 
            or (:endDate is null or t.start_date <= :endDate and t.end_date >= :endDate )
            and (:tourIds is null or t.id in (:tourIds))
            and (:tourParticipantIds is null or e.id in (:tourParticipantIds))
            group by tb.id, tb.tour_id, tb.tour_participant_id, tb.start_date, tb.end_date
            order by t.START_DATE, tb.START_DATE, t.END_DATE, tb.END_DATE      
            """)
    List<TourBind> findByFilter(LocalDateTime startDate, LocalDateTime endDate, List<Long> tourIds, List<Long> tourParticipantIds);
}
