package org.novinomad.picasso.repositories.jpa;

import org.novinomad.picasso.erm.entities.TourParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface TourParticipantRepository extends JpaRepository<TourParticipant, Long> {

    List<TourParticipant> findAllByTypeIn(Collection<TourParticipant.Type> types);

    @Query("from TourParticipant tp where tp.type = :type")
    List<TourParticipant> findAllByType(@Param("type") TourParticipant.Type type);

    List<TourParticipant> findAllByIdIn(List<Long> ids);

    @Query("from TourParticipant tp where TYPE(tp) = :entityClass")
    List<TourParticipant> findAllByClass(String entityClass);
}
