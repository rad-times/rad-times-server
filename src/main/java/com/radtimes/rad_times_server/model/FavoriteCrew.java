package com.radtimes.rad_times_server.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="crew_favorites")
public class FavoriteCrew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer primary_id;
    private Integer favorite_id;
}
