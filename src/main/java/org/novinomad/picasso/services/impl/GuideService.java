package org.novinomad.picasso.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.erm.entities.Guide;
import org.novinomad.picasso.erm.entities.Guide;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.repositories.jpa.GuideRepository;
import org.novinomad.picasso.services.IGuideService;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuideService implements IGuideService {

    final GuideRepository guideRepository;

    final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Guide save(Guide guide) throws CommonRuntimeException {
        try {
            return guideRepository.save(guide);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to create {} because {}", guide, e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            guideRepository.deleteById(id);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete guide with id {} because {}", id, e.getMessage());
        }
    }

    @Override
    public void deleteById(Iterable<Long> ids) throws CommonRuntimeException {
        try {
            guideRepository.deleteAllById(ids);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete guides with ids {} because {}", ids, e.getMessage());
        }
    }

    /**
     * @implNote If guides not provided wil be removed all items. So be careful when using this method
     * */
    @Override
    public void delete(Guide... guides) throws CommonRuntimeException {
        try {
            if(guides == null || guides.length == 0) {
                guideRepository.deleteAll();
            } else {
                guideRepository.deleteAll(Arrays.asList(guides));
            }
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete guides {} because {}", guides, e.getMessage());
        }
    }

    /**
     * @implNote If ids not provided wil be returned all items.
     * */
    @Override
    public List<Guide> get(Long... ids) throws CommonRuntimeException {
        try {
            if(ids == null || ids.length == 0) {
                return guideRepository.findAll();
            } else {
                return guideRepository.findAllById(Arrays.asList(ids));
            }
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get guides by ids {} because {}", ids, e.getMessage());
        }
    }

    @Override
    public Optional<Guide> get(Long id) throws CommonRuntimeException {
        try {
            return guideRepository.findById(id);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get Guide by id {} because {}", id, e.getMessage());
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
