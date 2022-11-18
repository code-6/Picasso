package org.novinomad.picasso.repositories;

import lombok.RequiredArgsConstructor;
import org.novinomad.picasso.erm.entities.TourBind;
import org.novinomad.picasso.repositories.jpa.TourBindJpaRepository;
import org.novinomad.picasso.services.conversion.rowmappers.TourBindRowMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.localDateTimeToDate;

@Repository
@RequiredArgsConstructor
public class TourBindRepositoryImpl implements TourBindRepository {

    private final TourBindJpaRepository jpaRepository;

    private final TourBindRowMapper tourBindRowMapper;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<TourBind> findByFilter(LocalDateTime startDate, LocalDateTime endDate, List<Long> tourIds, List<Long> tourParticipantIds) {
        StringBuilder sql = new StringBuilder("""
                select tb.id, tb.tour_id, tb.tour_participant_id, tb.start_date, tb.end_date
                from TOUR_BIND tb
                join TOUR t on tb.tour_id = t.id
                left join TOUR_PARTICIPANT e on tb.TOUR_PARTICIPANT_ID = e.ID
                where (:endDate is null or t.start_date <= :endDate)
                and (:startDate is null or t.end_date >= :startDate)
                """);

        if (!CollectionUtils.isEmpty(tourIds)) {
            sql.append(" and (t.id in (:tourIds)) ");
        }

        if (!CollectionUtils.isEmpty(tourParticipantIds)) {
            sql.append(" and (e.id in (:tourParticipantIds)) ");
        }

        sql.append("""                                            
                group by tb.id, tb.tour_id, tb.tour_participant_id, tb.start_date, tb.end_date
                order by t.START_DATE, tb.START_DATE, t.END_DATE, tb.END_DATE   
                """);

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("startDate", localDateTimeToDate(startDate))
                .addValue("endDate", localDateTimeToDate(endDate))
                .addValue("tourIds", tourIds)
                .addValue("tourParticipantIds", tourParticipantIds);

        return jdbcTemplate.query(sql.toString(), params, tourBindRowMapper);
    }

    @Override
    public List<TourBind> findOverlappedBinds(Long tourId, Long tourParticipantId, LocalDateTime newBindStartDate, LocalDateTime newBindEndDate) {
        return jpaRepository.findOverlappedBinds(tourId, tourParticipantId, newBindStartDate, newBindEndDate);
    }


    @Override
    public List<TourBind> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<TourBind> findAll(Sort sort) {
        return jpaRepository.findAll(sort);
    }

    @Override
    public Page<TourBind> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public List<TourBind> findAllById(Iterable<Long> longs) {
        return jpaRepository.findAllById(longs);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public void deleteById(Long aLong) {
        jpaRepository.deleteById(aLong);
    }

    @Override
    public void delete(TourBind entity) {
        jpaRepository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        jpaRepository.deleteAllById(longs);
    }

    @Override
    public void deleteAll(Iterable<? extends TourBind> entities) {
        jpaRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public <S extends TourBind> S save(S entity) {
        return jpaRepository.save(entity);
    }

    @Override
    public <S extends TourBind> List<S> saveAll(Iterable<S> entities) {
        return jpaRepository.saveAll(entities);
    }

    @Override
    public Optional<TourBind> findById(Long aLong) {
        return jpaRepository.findById(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        return jpaRepository.existsById(aLong);
    }

    @Override
    public void flush() {
        jpaRepository.flush();
    }

    @Override
    public <S extends TourBind> S saveAndFlush(S entity) {
        return jpaRepository.saveAndFlush(entity);
    }

    @Override
    public <S extends TourBind> List<S> saveAllAndFlush(Iterable<S> entities) {
        return jpaRepository.saveAllAndFlush(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<TourBind> entities) {
        jpaRepository.deleteAllInBatch(entities);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
        jpaRepository.deleteAllByIdInBatch(longs);
    }

    @Override
    public void deleteAllInBatch() {
        jpaRepository.deleteAllInBatch();
    }

    @Override
    public TourBind getOne(Long aLong) {
        return jpaRepository.getOne(aLong);
    }

    @Override
    public TourBind getById(Long aLong) {
        return jpaRepository.getById(aLong);
    }

    @Override
    public TourBind getReferenceById(Long aLong) {
        return jpaRepository.getReferenceById(aLong);
    }

    @Override
    public <S extends TourBind> Optional<S> findOne(Example<S> example) {
        return jpaRepository.findOne(example);
    }

    @Override
    public <S extends TourBind> List<S> findAll(Example<S> example) {
        return jpaRepository.findAll(example);
    }

    @Override
    public <S extends TourBind> List<S> findAll(Example<S> example, Sort sort) {
        return jpaRepository.findAll(example, sort);
    }

    @Override
    public <S extends TourBind> Page<S> findAll(Example<S> example, Pageable pageable) {
        return jpaRepository.findAll(example, pageable);
    }

    @Override
    public <S extends TourBind> long count(Example<S> example) {
        return jpaRepository.count(example);
    }

    @Override
    public <S extends TourBind> boolean exists(Example<S> example) {
        return jpaRepository.exists(example);
    }

    @Override
    public <S extends TourBind, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return jpaRepository.findBy(example, queryFunction);
    }
}
