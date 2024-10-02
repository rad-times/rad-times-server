package com.spotcheck.spotcheck_server.service;

import com.spotcheck.spotcheck_server.model.PersonModel;
import com.spotcheck.spotcheck_server.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CrewService {

    private final PersonRepository personRepository;

    public CrewService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    // Return all friends by / for ID
    public Optional<Set<PersonModel>> getCrewByPersonId(Integer id) {
        try {
            return personRepository.getCrewListForPersonId(id);

        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to fetch crew by person ID: " + e.getMessage());
        }
    }

}
