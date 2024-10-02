package com.spotcheck.spotcheck_server.service;

import com.spotcheck.spotcheck_server.model.Spot;
import com.spotcheck.spotcheck_server.repository.SpotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpotService {
    private final SpotRepository spotRepository;

    public SpotService(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }
    // Get a spot by ID
    public Optional<Spot> getSpotById(Integer id) {
        try {
            return spotRepository.findById(id);
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to fetch spot by ID: " + e.getMessage());
        }
    }

    public Optional<List<Spot>> getSpotsByName(String nameToMatch) {
        try {
            if (nameToMatch == null || nameToMatch.isEmpty()) {
                return spotRepository.getFavoriteSpots(1);
            }
            return spotRepository.getSpotsByNameLike(nameToMatch, 1);
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to fetch spot by ID: " + e.getMessage());
        }
    }

    public Optional<List<Spot>> getFavoriteSpotsOnly() {
        try {
            return spotRepository.getFavoriteSpots(1);
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to fetch spot by ID: " + e.getMessage());
        }
    }

    public Optional<List<Spot>> getPrivateSpotsOnly() {
        try {
            return spotRepository.getPrivateSpots(1);
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to fetch spot by ID: " + e.getMessage());
        }
    }
}
