package com.radtimes.rad_times_server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name="person")
public class PersonModel {
    public enum LanguageLocale {
        EN, ES, FR
    }
    public enum UserStatus {
        PENDING, ACTIVE, DELETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    private String refresh_token;

    private String first_name;
    private String last_name;
    private String email;
    private String bio;
    private String profile_image;
    private LanguageLocale language_code;

    private UserStatus status;

    @Transient
    private Boolean is_favorite;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Set<SocialMediaLinks> socials;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="location_id", referencedColumnName="id")
    private PersonLocation location;

    @ManyToMany
    private Set<PersonModel> crew;
}
