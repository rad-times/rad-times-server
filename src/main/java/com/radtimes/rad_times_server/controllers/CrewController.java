package com.radtimes.rad_times_server.controllers;

import com.radtimes.rad_times_server.model.PersonModel;
import com.radtimes.rad_times_server.service.CrewService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;
import java.util.Set;

@Controller
public class CrewController {
    private final CrewService crewService;

    public CrewController(CrewService crewService) {
        this.crewService = crewService;
    }

    @QueryMapping
    public Optional<Set<PersonModel>> crewByPersonId(@Argument Integer id) {
        return crewService.getCrewByPersonId(id);
    }
}
