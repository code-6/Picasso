package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {


    @Query(value = """
            select t from Tour t 
            where (:startDate is null or t.dateTimeRange.startDate >= :startDate) 
              and (:endDate is null or t.dateTimeRange.endDate <= :endDate)
              and (t.id in (:tourIds) or :tourIds is null ) 
              and t.deleted = false
            """)
    List<Tour> findByFilter(@Param("startDate") LocalDateTime startDate,
                            @Param("endDate") LocalDateTime endDate,
                            @Param("tourIds") List<Long> tourIds);

    @Query("select t from Tour t order by t.dateTimeRange.startDate desc ")
    List<Tour> findAll();

    @Query("select t from Tour t where t.id in (:ids)")
    List<Tour> findAllById(Collection<Long> ids);

    @Query("update Tour set deleted = true where id = :id")
    @Modifying
    int softDeleteById(Long id);

    @Query("update Tour set deleted = true where id in(:ids)")
    @Modifying
    int softDeleteById(Collection<Long> ids);
}
