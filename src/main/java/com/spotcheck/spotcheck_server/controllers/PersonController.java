package com.spotcheck.spotcheck_server.controllers;

import com.spotcheck.spotcheck_server.model.PersonModel;

import com.spotcheck.spotcheck_server.service.GeolocationService;
import com.spotcheck.spotcheck_server.service.PersonService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @QueryMapping
    public Optional<PersonModel> personById(@Argument Integer id) {
        return personService.getPlayerById(id);
    }

    @QueryMapping
    public Optional<List<PersonModel>> searchPersonsByName(@Argument String nameToMatch) {
        return personService.searchPersonsByName(nameToMatch);
    }
}
