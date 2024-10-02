package com.spotcheck.spotcheck_server.controllers;

import com.spotcheck.spotcheck_server.model.Spot;
import com.spotcheck.spotcheck_server.service.KeywordService;
import com.spotcheck.spotcheck_server.service.SpotService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class SpotController {
    private final SpotService spotService;
    private final KeywordService keywordService;

    public SpotController(SpotService spotService, KeywordService keywordService) {
        this.spotService = spotService;
        this.keywordService = keywordService;
    }

    @QueryMapping
    public Optional<Spot> spotById(@Argument Integer id) {
        return spotService.getSpotById(id);
    }

    @QueryMapping
    public Optional<List<Spot>> spotByName(@Argument String nameToMatch) {
        return spotService.getSpotsByName(nameToMatch);
    }

    @QueryMapping
    public Optional<List<Spot>> spotByFavoriteOnly() {
        return spotService.getFavoriteSpotsOnly();
    }

    @QueryMapping
    public Optional<List<Spot>> spotByPrivateOnly() {
        return spotService.getPrivateSpotsOnly();
    }
}
