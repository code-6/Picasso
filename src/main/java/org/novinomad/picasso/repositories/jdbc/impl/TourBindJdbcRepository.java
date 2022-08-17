package org.novinomad.picasso.repositories.jdbc.impl;

import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.repositories.jpa.TourBindRepository;
import org.novinomad.picasso.services.conversion.rowmappers.TourBindRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Qualifier("tourBindJdbcRepository")
public class TourBindJdbcRepository implements TourBindRepository {

    @Autowired
    private TourBindRowMapper tourBindRowMapper;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;


    @Override
    public List<TourBind> findOverlappedBinds(Long employeeId, LocalDateTime newBindStartDate, LocalDateTime newBindEndDate) {

        String sql = """
                select tb.* from TOUR_BIND tb
                where (tb.START_DATE <= :newBindEndDate and tb.END_DATE >= :newBindStartDate)
                and tb.EMPLOYEE_ID = :employeeId
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("newBindEndDate", localDateTimeToDate(newBindEndDate))
                .addValue("newBindStartDate", localDateTimeToDate(newBindStartDate))
                .addValue("employeeId", employeeId);

        return jdbcTemplate.query(sql, params, tourBindRowMapper);
    }

    @Override
    public List<TourBind> findByFilter(LocalDateTime startDate, LocalDateTime endDate, List<Long> tourIds, List<Long> employeeIds) {
        StringBuilder sql = new StringBuilder("""
                select tb.id, tb.tour_id, tb.employee_id, tb.start_date, tb.end_date
                from TOUR_BIND tb
                join TOUR t on tb.tour_id = t.id
                left join EMPLOYEE e on tb.EMPLOYEE_ID = e.ID
                where (:endDate is null or t.start_date <= :endDate)
                and (:startDate is null or t.end_date >= :startDate)
                """);

        if (!CollectionUtils.isEmpty(tourIds)) {
            sql.append(" and (t.id in (:tourIds)) ");
        }

        if (!CollectionUtils.isEmpty(employeeIds)) {
            sql.append(" and (e.id in (:employeeIds)) ");
        }

        sql.append("""                                            
                group by tb.id, tb.tour_id, tb.employee_id, tb.start_date, tb.end_date
                order by t.START_DATE, tb.START_DATE, t.END_DATE, tb.END_DATE   
                """);

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("startDate", localDateTimeToDate(startDate))
                .addValue("endDate", localDateTimeToDate(endDate))
                .addValue("tourIds", tourIds)
                .addValue("employeeIds", employeeIds);

        return jdbcTemplate.query(sql.toString(), params, tourBindRowMapper);
    }

    @Override
    public List<TourBind> findAll() {
        return null;
    }

    @Override
    public List<TourBind> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<TourBind> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<TourBind> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(TourBind entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends TourBind> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends TourBind> S save(S entity) {
        return null;
    }

    @Override
    public <S extends TourBind> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<TourBind> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends TourBind> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends TourBind> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<TourBind> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public TourBind getOne(Long aLong) {
        return null;
    }

    @Override
    public TourBind getById(Long aLong) {
        return null;
    }

    @Override
    public TourBind getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends TourBind> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends TourBind> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends TourBind> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends TourBind> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends TourBind> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends TourBind> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends TourBind, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
