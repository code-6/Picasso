package org.novinomad.picasso.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.entities.domain.impl.Guide;
import org.novinomad.picasso.exceptions.base.BaseException;
import org.novinomad.picasso.repositories.jpa.GuideRepository;
import org.novinomad.picasso.services.IGuideService;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuideService implements IGuideService {

    final GuideRepository guideRepository;

    final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Guide save(Guide guide) throws BaseException {
        try {
            Guide savedGuide = guideRepository.save(guide);
            log.debug("saved {}", savedGuide);
            return savedGuide;
        } catch (Exception e) {
            log.error("unable to create: {} because: {}", guide, e.getMessage(), e);
            throw new BaseException(e, "unable to create: {} because: {}", guide, e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws BaseException {
        try {
            guideRepository.deleteById(id);
        } catch (Exception e) {
            log.error("unable to delete Guide with id: {} because: {}", id, e.getMessage(), e);
            throw new BaseException(e, "unable to delete Guide with id: {} because: {}", id, e.getMessage());
        }
    }

    @Override
    public List<String> getAllLanguages() {
        String selectAllLanguages = """
                select l.LANGUAGES from GUIDE_LANGUAGES l
                group by l.LANGUAGES
                """;
        return namedParameterJdbcTemplate.query(selectAllLanguages, new MapSqlParameterSource(),
                (rs, i) -> rs.getString("LANGUAGES"));
    }
}
