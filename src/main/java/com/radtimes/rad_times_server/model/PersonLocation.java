package com.radtimes.rad_times_server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="person_location")
public class PersonLocation {
    @Id
    @GeneratedValue
    private Integer id;

    private Double lat;
    private Double lng;

    private String city_id;
    private String city_name;
    private String state_name;
    private String country_name;
}
