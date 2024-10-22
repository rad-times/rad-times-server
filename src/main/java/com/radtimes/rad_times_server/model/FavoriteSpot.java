package com.radtimes.rad_times_server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="favorite_spots")
public class FavoriteSpot {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer person_id;
    private Integer spot_id;
}
