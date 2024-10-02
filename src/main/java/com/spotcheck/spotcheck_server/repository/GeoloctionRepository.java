package com.spotcheck.spotcheck_server.repository;

import com.spotcheck.spotcheck_server.model.Geolocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoloctionRepository extends JpaRepository<Geolocation, Integer> {
}
