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

    private Boolean is_favorite;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Set<SocialMediaLinks> socials;

    @OneToOne
    @JoinColumn(name="location_id", referencedColumnName="location_id")
    private Geolocation location;

    @ManyToMany
    private Set<PersonModel> crew;
}
