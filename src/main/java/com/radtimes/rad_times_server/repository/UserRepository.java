package com.radtimes.rad_times_server.repository;

import com.radtimes.rad_times_server.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<PersonModel, Long> {

    Boolean existsByEmail(String email);

    Optional<PersonModel> findByEmail(String email);

}
