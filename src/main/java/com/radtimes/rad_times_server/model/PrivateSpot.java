package com.radtimes.rad_times_server.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="private_spots")
public class PrivateSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer person_id;
    private Integer spot_id;
}
