package com.radtimes.rad_times_server.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="spot_location")
public class SpotLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer location_id;

    private Double lat;
    private Double lng;

    private String city_name;
    private String street_number;
    private String street_name;
    private String state_name;
    private String country_name;
    private String postal_code;
}
