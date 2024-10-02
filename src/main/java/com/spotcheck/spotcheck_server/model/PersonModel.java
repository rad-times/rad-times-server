package com.spotcheck.spotcheck_server.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name="person")
public class PersonModel {
    @Id
    @GeneratedValue
    private Integer id;

    private String first_name;
    private String last_name;
    private String email_address;
    private String bio;
    private Integer status;
    private String profile_image;

    @OneToOne
    @JoinColumn(name="location_id", referencedColumnName="location_id")
    private Geolocation location;

    @ManyToMany
    @JoinTable(
            name = "crew",
            joinColumns = @JoinColumn(name="person_one"),
            inverseJoinColumns = @JoinColumn(name="person_two")
    )
    private Set<PersonModel> crew;
}
