package com.spotcheck.spotcheck_server.controllers;

import com.spotcheck.spotcheck_server.model.Spot;
import com.spotcheck.spotcheck_server.service.SpotService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class SpotController {
    private final SpotService spotService;

    public SpotController(SpotService spotService) {
        this.spotService = spotService;
    }

    @QueryMapping
    public Optional<Spot> spotById(@Argument Integer id) {
        return spotService.getSpotById(id);
    }

    @QueryMapping
    public Optional<List<Spot>> spotByName(@Argument String nameToMatch, @Argument Integer activeUserId) {
        return spotService.getSpotsByName(nameToMatch, activeUserId);
    }

    @QueryMapping
    public Optional<List<Spot>> spotByFavoriteOnly() {
        return spotService.getFavoriteSpotsOnly();
    }

    @QueryMapping
    public Optional<List<Spot>> spotByPrivateOnly(@Argument Integer activeUserId) {
        return spotService.getPrivateSpotsOnly(activeUserId);
    }

    @QueryMapping
    public List<Spot> spotByLatLng(@Argument Float lat, @Argument Float lng, @Argument Integer distance, @Argument Integer activeUserId) {
        return spotService.getSpotByLatLng(lat, lng, distance, activeUserId);
    }

    @MutationMapping
    public Optional<Spot> toggleSpotFavorite(@Argument Integer spotId, @Argument Integer activeUserId, @Argument Boolean isFavorite) {
        return spotService.toggleSpotFavorite(spotId, activeUserId, isFavorite);
    }
}
