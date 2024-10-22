package com.radtimes.rad_times_server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="crew_favorites")
public class FavoriteCrew {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer primary_id;
    private Integer favorite_id;
}
