package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.entities.domain.impl.TourParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourParticipantRepository extends JpaRepository<TourParticipant, Long> {

    List<TourParticipant> findAllByTypeIn(List<TourParticipant.Type> types);

    @Query("from TourParticipant tp where tp.type = :type")
    <T extends TourParticipant> List<T> findAllByType(@Param("type") TourParticipant.Type type);

    List<TourParticipant> findAllByIdIn(List<Long> ids);

    @Query("from TourParticipant tp where TYPE(tp) = :entityClass")
    <T extends TourParticipant> List<T> findAllByClass(String entityClass);

}
