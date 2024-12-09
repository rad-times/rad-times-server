package com.radtimes.rad_times_server.service;

import com.google.api.client.auth.openidconnect.IdToken;
import com.radtimes.rad_times_server.model.FavoriteCrew;
import com.radtimes.rad_times_server.model.PersonModel;
import com.radtimes.rad_times_server.model.oauth.FacebookTokenPayload;
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
     * Use the user ID returned from oAuth to find a person record
     */
    public Optional<PersonModel> findPersonByEmail(String email) {
        try {
            return personRepository.findByEmail(email);
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to fetch player by User ID: " + e.getMessage());
        }
    }
    /**
     * Save the user's current refresh token
     */
    public Optional<Void> saveRefreshToken(String token, String email) {
        try {
            return personRepository.saveRefreshToken(token, email);
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to save user's refresh token: " + e.getMessage());
        }
    }
    /**
     * Get the user's current refresh token
     */
    public Optional<String> getRefreshToken(String email) {
        try {
            return personRepository.getRefreshToken(email);
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to get user's refresh token: " + e.getMessage());
        }
    }
    /**
     * Remove the saved refresh token
     */
    public Optional<Void> clearRefreshToken(String email) {
        try {
            return personRepository.saveRefreshToken(null, email);
        } catch (Exception e) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to get user's refresh token: " + e.getMessage());
        }
    }
    /**
     * Gets the user data for the person using the application
     */
    public Optional<PersonModel> getActivePersonByEmail(String email) {
        try {
            Optional<PersonModel> matchingPerson = personRepository.findByEmail(email);
            if (matchingPerson.isPresent()) {
                PersonModel person = matchingPerson.get();
                Optional<Set<PersonModel>> crewRequest = crewService.getCrewByPersonId(person.getId());
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
            throw new RuntimeException("Failed to fetch player by email: " + e.getMessage());
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
     * Create a new person record from oAuth sign in request
     */
    public PersonModel createPersonFromGoogleData(IdToken.Payload personData) {
        PersonModel newPerson = new PersonModel();

        newPerson.setEmail((String) personData.get("email"));
        newPerson.setFirst_name((String) personData.get("given_name"));
        newPerson.setLast_name((String) personData.get("family_name"));
        newPerson.setProfile_image((String) personData.get("picture"));

        // Locale appears to not be sent any longer. Start the user on EN
        newPerson.setLanguage_code(PersonModel.LanguageLocale.EN);
        newPerson.setStatus(PersonModel.UserStatus.PENDING);

        personRepository.save(newPerson);
        return newPerson;
    }
    /**
     * Create a new person record from oAuth sign in request
     */
    public PersonModel createPersonFromFacebookData(FacebookTokenPayload personData) {
        PersonModel newPerson = new PersonModel();

        newPerson.setEmail(personData.getEmail());
        newPerson.setFirst_name(personData.getGiven_name());
        newPerson.setLast_name(personData.getFamily_name());
        newPerson.setProfile_image(personData.getPicture());

        // Locale appears to not be sent any longer. Start the user on EN
        newPerson.setLanguage_code(PersonModel.LanguageLocale.EN);
        newPerson.setStatus(PersonModel.UserStatus.PENDING);

        personRepository.save(newPerson);
        return newPerson;
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
    /**
     * Apply edits to a person
     */
    public Optional<PersonModel> updatePersonById(Integer id, PersonModel updatedPerson) {
        PersonModel personToUpdate = personRepository.findById(id).orElse(null);

        if (personToUpdate == null) {
            return Optional.empty();
        }

        if (updatedPerson.getFirst_name() != null) {
            personToUpdate.setFirst_name(updatedPerson.getFirst_name());
        }

        if (updatedPerson.getLast_name() != null) {
            personToUpdate.setLast_name(updatedPerson.getLast_name());
        }

        if (updatedPerson.getBio() != null) {
            personToUpdate.setBio(updatedPerson.getBio());
        }

        if (updatedPerson.getLocation() != null) {
            personToUpdate.setLocation(updatedPerson.getLocation());
        }

        personRepository.save(personToUpdate);
        return Optional.of(personToUpdate);
    }
}
