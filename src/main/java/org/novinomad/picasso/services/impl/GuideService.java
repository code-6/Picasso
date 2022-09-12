package org.novinomad.picasso.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.entities.domain.impl.Guide;
import org.novinomad.picasso.exceptions.base.PicassoException;
import org.novinomad.picasso.repositories.jpa.GuideRepository;
import org.novinomad.picasso.services.IGuideService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuideService implements IGuideService {

    final GuideRepository guideRepository;

    @Override
    public Guide save(Guide guide) throws PicassoException {
        try {
            Guide savedGuide = guideRepository.save(guide);
            log.debug("saved {}", savedGuide);
            return savedGuide;
        } catch (Exception e) {
            log.error("unable to create: {} because: {}", guide, e.getMessage(), e);
            throw new PicassoException(e, "unable to create: {} because: {}", guide, e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws PicassoException {
        try {
            guideRepository.deleteById(id);
        } catch (Exception e) {
            log.error("unable to delete Guide with id: {} because: {}", id, e.getMessage(), e);
            throw new PicassoException(e, "unable to delete Guide with id: {} because: {}", id, e.getMessage());
        }
    }
}
