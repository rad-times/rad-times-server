package com.radtimes.rad_times_server.controllers;

import com.radtimes.rad_times_server.model.PersonModel;

import com.radtimes.rad_times_server.service.PersonService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

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
    public Optional<PersonModel> activePersonByEmail(@Argument String email) {
        return personService.getActivePersonByEmail(email);
    }

    @QueryMapping
    public Optional<List<PersonModel>> searchPersonsByName(@Argument String nameToMatch) {
        return personService.searchPersonsByName(nameToMatch);
    }

    @MutationMapping
    public Optional<PersonModel> togglePersonFavorite(@Argument Integer personId, @Argument Integer activeUserId, @Argument Boolean isFavorite) {
        return personService.togglePersonFavorite(personId, activeUserId, isFavorite);
    }

    @PutMapping("/users/{id}")
    public Optional<PersonModel> updatePersonById(@PathVariable Integer id, @RequestBody PersonModel updatePayload) {
        return personService.updatePersonById(id, updatePayload);
    }
}
