package com.radtimes.rad_times_server.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="social_media")
public class SocialMediaLinks {
    private enum SocialType {
        FACEBOOK, INSTAGRAM, YOUTUBE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer person_id;

    @Enumerated(EnumType.STRING)
    private SocialType social_type;
    private String url_link;
}
