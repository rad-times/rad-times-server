package com.spotcheck.spotcheck_server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="private_spots")
public class PrivateSpot {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer person_id;
    private Integer spot_id;
}
