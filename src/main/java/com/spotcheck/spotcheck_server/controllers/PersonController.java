package com.spotcheck.spotcheck_server.controllers;

import com.spotcheck.spotcheck_server.model.PersonModel;

import com.spotcheck.spotcheck_server.service.PersonService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
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
        return personService.getPersonById(id);
    }

    @QueryMapping
    public Optional<PersonModel> activePersonById(@Argument Integer id) {
        return personService.getActivePersonById(id);
    }

    @QueryMapping
    public Optional<List<PersonModel>> searchPersonsByName(@Argument String nameToMatch) {
        return personService.searchPersonsByName(nameToMatch);
    }


    @MutationMapping
    public Optional<PersonModel> togglePersonFavorite(@Argument Integer personId, @Argument Integer activeUserId, @Argument Boolean isFavorite) {
        return personService.togglePersonFavorite(personId, activeUserId, isFavorite);
    }
}
