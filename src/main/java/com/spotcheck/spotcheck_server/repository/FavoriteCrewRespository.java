package com.spotcheck.spotcheck_server.repository;

import com.spotcheck.spotcheck_server.model.FavoriteCrew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface FavoriteCrewRespository extends JpaRepository<FavoriteCrew, Integer> {
    @Query("SELECT fc FROM FavoriteCrew fc WHERE fc.primary_id = :activeUserId")
    Optional<Set<FavoriteCrew>> getAllFavoritesByActiveUserId(@Param("activeUserId") Integer activeUserId);

    @Query("SELECT fc FROM FavoriteCrew fc WHERE fc.primary_id = :activeUserId AND fc.favorite_id = :personId")
    Optional<FavoriteCrew> getFavoritePersonListing(@Param("activeUserId") Integer activeUserId, @Param("personId") Integer personId);
}
