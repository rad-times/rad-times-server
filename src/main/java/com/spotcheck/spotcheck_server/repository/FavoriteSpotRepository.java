package com.spotcheck.spotcheck_server.repository;

import com.spotcheck.spotcheck_server.model.FavoriteSpot;
import com.spotcheck.spotcheck_server.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteSpotRepository extends JpaRepository<FavoriteSpot, Integer> {
    @Query("SELECT fv FROM FavoriteSpot fv WHERE fv.person_id = :personId AND fv.spot_id = :spotId")
    Optional<FavoriteSpot> findBySpotAndPersonId(@Param("spotId") Integer spotId, @Param("personId") Integer personId);
}
