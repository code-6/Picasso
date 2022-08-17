package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.domain.entities.impl.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {


    @Query(nativeQuery = true, value = """
            select * from tour t 
            where (:startDate is null or t.start_date >= :startDate) 
              and (:endDate is null or t.end_date <= :endDate)
              and (:name is null or lower(t.name) like :name) 
            """)
    List<Tour> findByFilter(LocalDateTime startDate, LocalDateTime endDate, String name);

    @Query("select t from Tour t order by t.startDate desc ")
    @Override
    List<Tour> findAll();
}
