package com.radtimes.rad_times_server.service;

import com.radtimes.rad_times_server.model.footage.ClipModel;
import com.radtimes.rad_times_server.repository.FootageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class FootageService {
    private final FootageRepository footageRepository;

    public FootageService(FootageRepository footageRepository) {
        this.footageRepository = footageRepository;
    }

    public Set<ClipModel> loadClipSet(Long startDateTime, Integer count, List<String> filters) {
        try {
            Limit limit = Limit.of(count);
            Optional<Set<ClipModel>> clips = footageRepository.findByCreateDateBeforeOrderByCreateDateDesc(startDateTime, limit);

            return clips.orElseGet(Set::of);

        } catch (Exception err) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to get set of clips from request: " + err.getMessage());
        }

    }

}
