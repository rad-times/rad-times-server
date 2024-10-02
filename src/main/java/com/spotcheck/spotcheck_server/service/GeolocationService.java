package com.spotcheck.spotcheck_server.service;

import com.spotcheck.spotcheck_server.model.Geolocation;
import com.spotcheck.spotcheck_server.repository.GeoloctionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GeolocationService {
    private final GeoloctionRepository geoloctionRepository;

    public GeolocationService(GeoloctionRepository geoloctionRepository) {
        this.geoloctionRepository = geoloctionRepository;
    }
    // Get a product by ID
    public Optional<Geolocation> getLocationById(Integer id) {
        try {
            return geoloctionRepository.findById(id);
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to fetch location by ID: " + e.getMessage());
        }
    }

}
