package com.radtimes.rad_times_server.controllers;

import com.radtimes.rad_times_server.model.PersonModel;
import com.radtimes.rad_times_server.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/person")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PutMapping("/{id}")
    public Optional<PersonModel> updatePersonById(@PathVariable Integer id, @RequestBody PersonModel updatePayload) {
        return personService.updatePersonById(id, updatePayload);
    }
}
