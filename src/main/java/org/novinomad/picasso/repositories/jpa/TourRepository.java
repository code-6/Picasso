package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.entities.domain.impl.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {


    @Query(nativeQuery = true, value = """
            select * from tour t 
            where (:startDate is null or t.start_date >= :startDate) 
              and (:endDate is null or t.end_date <= :endDate)
              and (t.id in (:tourIds) or :tourIds is null ) 
            """)
    List<Tour> findByFilter(@Param("startDate") LocalDateTime startDate,
                            @Param("endDate") LocalDateTime endDate,
                            @Param("tourIds") List<Long> tourIds);

    @Query("select t from Tour t order by t.startDate desc ")
    @Override
    List<Tour> findAll();
}
