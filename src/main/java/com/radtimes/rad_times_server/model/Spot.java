package com.radtimes.rad_times_server.model;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer spot_id;

    private String spot_name;
    private String spot_image;
    private String spot_description;
    private Date last_check_in;

    @Enumerated(EnumType.STRING)
    private Popularity popularity;

    private Integer popularity_rating_count;
    private Boolean is_public;
    private Boolean is_private;
    private Boolean is_favorite;

    @Column(insertable = false, updatable = false)
    private Integer location_id;

    @OneToOne
    @JoinColumn(name="location_id", referencedColumnName="location_id")
    private SpotLocation location;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "spot_keywords",
            joinColumns = @JoinColumn(name="spot_id"),
            inverseJoinColumns = @JoinColumn(name="keyword_id")
    )
    private Set<Keyword> keywords;
}
