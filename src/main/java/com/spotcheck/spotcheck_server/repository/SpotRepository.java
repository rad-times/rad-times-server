package com.spotcheck.spotcheck_server.repository;

import com.spotcheck.spotcheck_server.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Integer> {

    @Query("SELECT sp FROM Spot sp WHERE sp.spot_name LIKE %:nameToMatch% AND (sp.is_private <> true OR (sp.is_private = true AND sp.spot_id IN (SELECT spt.spot_id FROM Spot spt JOIN PrivateSpot pvt ON pvt.spot_id = spt.spot_id WHERE pvt.person_id = :personId)))")
    Optional<List<Spot>> getSpotsByNameLike(@Param("nameToMatch") String nameToMatch, @Param("personId") Integer personId);

    @Query("SELECT sp FROM Spot sp JOIN FavoriteSpot fv ON fv.spot_id = sp.spot_id WHERE fv.person_id = :personId")
    Optional<List<Spot>> getFavoriteSpots(@Param("personId") Integer personId);

    @Query("SELECT sp FROM Spot sp JOIN PrivateSpot pvt ON pvt.spot_id = sp.spot_id WHERE pvt.person_id = :personId")
    Optional<List<Spot>> getPrivateSpots(@Param("personId") Integer personId);
}
