package com.spotcheck.spotcheck_server.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name="spot")
public class Spot {
    private enum Popularity {
        HIGH, MEDIUM, LOW, NONE
    }

    @Id
    @GeneratedValue
    private Integer spot_id;

    private String spot_name;
    private String spot_image;
    private String spot_description;
    private Date last_check_in;
    private Popularity popularity;
    private Integer popularity_rating_count;
    private Boolean is_public;
    private Boolean is_private;
    private Boolean is_favorite;

    @OneToOne
    @JoinColumn(name="location_id", referencedColumnName="location_id")
    private Geolocation location;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "spot_keywords",
            joinColumns = @JoinColumn(name="spot_id"),
            inverseJoinColumns = @JoinColumn(name="keyword_id")
    )
    private Set<Keyword> keywords;
}
