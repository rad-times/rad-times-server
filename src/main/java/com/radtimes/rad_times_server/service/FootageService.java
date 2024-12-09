package com.radtimes.rad_times_server.service;

import com.radtimes.rad_times_server.repository.FootageRepository;
import org.springframework.stereotype.Service;

@Service
public class FootageService {
    private final FootageRepository footageRepository;

    public FootageService(FootageRepository footageRepository) {
        this.footageRepository = footageRepository;
    }

}
