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
}
