package com.radtimes.rad_times_server.repository;

import com.radtimes.rad_times_server.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PersonRepository extends JpaRepository<PersonModel, Integer>  {

    @Query("SELECT p FROM PersonModel p WHERE concat(p.first_name, ' ',  p.last_name) LIKE %:nameToMatch%")
    Optional<List<PersonModel>> searchByPersonNameLike(@Param("nameToMatch") String nameToMatch);

    @Query("SELECT p2 FROM PersonModel p2 JOIN Crew cr ON p2.id = cr.person_two JOIN PersonModel p1 ON cr.person_one = p1.id WHERE p1.id = :id UNION SELECT p1 FROM PersonModel p1 JOIN Crew cr ON p1.id = cr.person_one JOIN PersonModel p2 ON cr.person_two = p2.id WHERE p2.id = :id")
    Optional<Set<PersonModel>> getCrewListForPersonId(@Param("id") Integer id);
}
