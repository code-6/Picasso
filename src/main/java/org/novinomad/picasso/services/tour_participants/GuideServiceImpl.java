package org.novinomad.picasso.services.tour_participants;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Guide;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.repositories.jpa.GuideRepository;
import org.novinomad.picasso.services.AbstractCrudCacheService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuideServiceImpl extends AbstractCrudCacheService<Long, Guide> implements GuideService {

    final GuideRepository guideRepository;

    final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GuideServiceImpl(GuideRepository guideRepository, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(guideRepository);
        this.guideRepository = guideRepository;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<String> getLanguages() {
        String selectAllLanguages = """
                select distinct l.LANGUAGES from GUIDE_LANGUAGES l
                """;
        return namedParameterJdbcTemplate.query(selectAllLanguages, new MapSqlParameterSource(),
                (rs, i) -> rs.getString("LANGUAGES"));
    }
}
