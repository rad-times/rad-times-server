package com.spotcheck.spotcheck_server.service;

import com.spotcheck.spotcheck_server.model.FavoriteSpot;
import com.spotcheck.spotcheck_server.model.GenericGraphQLResponse;
import com.spotcheck.spotcheck_server.model.Spot;
import com.spotcheck.spotcheck_server.repository.FavoriteSpotRepository;
import com.spotcheck.spotcheck_server.repository.SpotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpotService {
    private final SpotRepository spotRepository;
    private final FavoriteSpotRepository favoriteSpotRepository;

    public SpotService(SpotRepository spotRepository, FavoriteSpotRepository favoriteSpotRepository) {
        this.spotRepository = spotRepository;
        this.favoriteSpotRepository = favoriteSpotRepository;
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
            Optional<List<Spot>> spotFavorites = spotRepository.getFavoriteSpots(1);
            spotFavorites.ifPresent(spots -> spots.forEach(spot -> {
                spot.setIs_favorite(true);
            }));

            if (nameToMatch == null || nameToMatch.isEmpty()) {
                return spotFavorites;
            }

            // If I make this request, the favorites are still shown with is_favorite: true. Why?
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

    public Optional<Spot> toggleSpotFavorite(Integer spotId, Integer personId, Boolean isFavorite) {
        try {
            Optional<Spot> spotRequest = getSpotById(spotId);

            if (spotRequest.isEmpty()) {
                return Optional.empty();
            }

            Spot spot = spotRequest.get();
            Optional<FavoriteSpot> spotInFavoriteList = favoriteSpotRepository.findBySpotAndPersonId(spotId, personId);

            if (spotInFavoriteList.isPresent() && !isFavorite) {
                favoriteSpotRepository.delete(spotInFavoriteList.get());
                spot.setIs_favorite(false);
            } else if (spotInFavoriteList.isEmpty() && isFavorite) {
                FavoriteSpot newFavorite = new FavoriteSpot();
                newFavorite.setSpot_id(spotId);
                newFavorite.setPerson_id(personId);
                favoriteSpotRepository.save(newFavorite);
                spot.setIs_favorite(true);
            } else {
                spot.setIs_favorite(isFavorite);
            }

            return spotRequest;

        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to fetch spot by ID: " + e.getMessage());
        }
    }
}