package com.radtimes.rad_times_server.repository;

import com.radtimes.rad_times_server.model.SpotLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotLocationRepository extends JpaRepository<SpotLocation, Integer> {
    @Query(value = "SELECT sploc.*,\n" +
            "  (\n" +
            "    3959 * acos(\n" +
            "        cos(radians(:lat))\n" +
            "        * cos(radians(lat))\n" +
            "        * cos(radians(lng) - radians(:lng))\n" +
            "        + sin(radians(:lat))\n" +
            "          * sin(radians(lat))\n" +
            "    )\n" +
            "  ) AS distance\n" +
            "FROM spot_location sploc \n" +
            "HAVING distance < :distance \n" +
            "ORDER BY distance asc",
          nativeQuery = true)
    List<SpotLocation> getSpotsByLatLng(@Param("lat") Float lat, @Param("lng") Float lng, @Param("distance") Integer distance);
}
