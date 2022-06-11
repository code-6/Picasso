package org.novinomad.picasso.services.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.exceptions.TourBindException;
import org.novinomad.picasso.repositories.TourBindRepository;
import org.novinomad.picasso.services.ITourBindService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourBindService implements ITourBindService {

    @Getter
    final TourBindRepository tourBindRepository;

    @Override
    public void bind(Employee employee, Tour tour, LocalDateTime startDate, LocalDateTime endDate) throws TourBindException {
        try {
            TourBind tourBind = new TourBind(employee, tour, startDate, endDate);
        } catch (TourBindException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean intersects(TourBind tourBind) throws TourBindException {
        throw new NotImplementedException();
    }
}
