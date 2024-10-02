package com.spotcheck.spotcheck_server.service;

import com.spotcheck.spotcheck_server.model.PersonModel;
import com.spotcheck.spotcheck_server.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonModel createPerson(PersonModel player) {
        try {
            return personRepository.save(player);
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to save player: " + e.getMessage());
        }
    }

    // Get all products
    public List<PersonModel> getAllPlayers() {
        try {
            return personRepository.findAll();
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to fetch all players: " + e.getMessage());
        }
    }

    // Get a product by ID
    public Optional<PersonModel> getPlayerById(Integer id) {
        try {
            return personRepository.findById(id);
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to fetch player by ID: " + e.getMessage());
        }
    }

    public Optional<PersonModel> updatePlayer(Integer id, PersonModel updatedPlayer) {
        try {
            Optional<PersonModel> existingPlayerOptional = personRepository.findById(id);
            if (existingPlayerOptional.isPresent()) {
                PersonModel existingProduct = existingPlayerOptional.get();
                existingProduct.setFirst_name(updatedPlayer.getFirst_name());
                existingProduct.setLast_name(updatedPlayer.getLast_name());
                existingProduct.setEmail_address(updatedPlayer.getEmail_address());
                PersonModel savedEntity = personRepository.save(existingProduct);
                return Optional.of(savedEntity);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to update player: " + e.getMessage());
        }
    }

    public boolean deletePlayer(Integer id) {
        try {
            personRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to delete player: " + e.getMessage());
        }
    }

    public Optional<List<PersonModel>> searchPersonsByName(String nameToMatch) {
        return personRepository.searchByPersonNameLike(nameToMatch);
    }
}
