package org.novinomad.picasso.repositories;

import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TourBindRepository extends JpaRepository<TourBind, Long> {

    @Query(nativeQuery = true, value = """
            select tb.* from TOUR_BIND tb
            where tb.START_DATE <= :newBindStartDate and tb.END_DATE >= :newBindEndDate
              and tb.EMPLOYEE_ID = :employeeId
            """)
    List<TourBind> findOverlapsBinds(Long employeeId,
                                     LocalDateTime newBindStartDate,
                                     LocalDateTime newBindEndDate);

    @Query(nativeQuery = true, value = """
            select tb.id, tb.tour_id, tb.employee_id, tb.start_date, tb.end_date
            from TOUR_BIND tb
            join TOUR t on tb.tour_id = t.id
            left join EMPLOYEE e on tb.EMPLOYEE_ID = e.ID
            where (:startDate is null or t.start_date <= :startDate and t.end_date >= :startDate )
            or ( (:startDate is null or t.start_date >= :startDate) and (:endDate is null or t.end_date <= :endDate) ) 
            or (:endDate is null or t.start_date <= :endDate and t.end_date >= :endDate )
            and (:tourIds is null or t.id in (:tourIds))
            and (:employeeIds is null or e.id in (:employeeIds))
            group by tb.id, tb.tour_id, tb.employee_id, tb.start_date, tb.end_date
            order by t.START_DATE, tb.START_DATE, t.END_DATE, tb.END_DATE      
            """)
    List<TourBind> findByDatesTourEmployee(LocalDateTime startDate, LocalDateTime endDate, List<Long> tourIds, List<Long> employeeIds);
}
