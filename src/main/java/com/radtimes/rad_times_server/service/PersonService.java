package com.radtimes.rad_times_server.service;

import com.radtimes.rad_times_server.model.FavoriteCrew;
import com.radtimes.rad_times_server.model.PersonModel;
import com.radtimes.rad_times_server.repository.FavoriteCrewRespository;
import com.radtimes.rad_times_server.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private static final Logger log = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;
    private final CrewService crewService;
    private final FavoriteCrewRespository favoriteCrewRespository;

    public PersonService(PersonRepository personRepository, FavoriteCrewRespository favoriteCrewRespository, CrewService crewService) {
        this.personRepository = personRepository;
        this.crewService = crewService;
        this.favoriteCrewRespository = favoriteCrewRespository;
    }
    /**
     * Gets the user data for the person using the application
     */
    public Optional<PersonModel> getActivePersonById(Integer id) {
        try {
            Optional<PersonModel> matchingPerson = personRepository.findById(id);
            if (matchingPerson.isPresent()) {
                PersonModel person = matchingPerson.get();
                Optional<Set<PersonModel>> crewRequest = crewService.getCrewByPersonId(id);
                if (crewRequest.isPresent()) {
                    Set<PersonModel> crewList = crewRequest.get();
                    Optional<Set<FavoriteCrew>> favoritesReq = favoriteCrewRespository.getAllFavoritesByActiveUserId(person.getId());
                    if (favoritesReq.isPresent()) {
                        Set<Integer> favoriteIds = favoritesReq.stream().flatMap(fv -> fv.stream().map(FavoriteCrew::getFavorite_id)).collect(Collectors.toSet());
                        crewList.forEach(personModel -> {
                            personModel.setIs_favorite(favoriteIds.contains(personModel.getId()));
                        });
                    }
                }
                person.setCrew(crewRequest.orElseGet(Set::of));
            }

            return matchingPerson;

        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to fetch player by ID: " + e.getMessage());
        }
    }
    /**
     * Get a person by ID
     */
    public Optional<PersonModel> getPersonById(Integer id) {
        try {
            return personRepository.findById(id);
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to fetch player by ID: " + e.getMessage());
        }
    }
    /**
     * Returns all persons matching the name param
     */
    public Optional<List<PersonModel>> searchPersonsByName(String nameToMatch) {
        return personRepository.searchByPersonNameLike(nameToMatch);
    }
    /**
     * Sets 'personId' as a favorite friend of the active user
     */
    public Optional<PersonModel> togglePersonFavorite(Integer personId, Integer activeUserId, boolean isFavorite) {
        try {
            Optional<PersonModel> personRequest = this.getPersonById(personId);

            if (personRequest.isEmpty()) {
                return Optional.empty();
            }

            PersonModel person = personRequest.get();
            Optional<FavoriteCrew> personInFavoriteList = favoriteCrewRespository.getFavoritePersonListing(activeUserId, personId);

            if (personInFavoriteList.isPresent() && !isFavorite) {
                favoriteCrewRespository.delete(personInFavoriteList.get());
                person.setIs_favorite(false);
            } else if (personInFavoriteList.isEmpty() && isFavorite) {
                FavoriteCrew newFavorite = new FavoriteCrew();
                newFavorite.setPrimary_id(activeUserId);
                newFavorite.setFavorite_id(personId);
                favoriteCrewRespository.save(newFavorite);
                person.setIs_favorite(true);
            } else {
                person.setIs_favorite(isFavorite);
            }

            return personRequest;

        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to set person as favorite: " + e.getMessage());
        }
    }
}
