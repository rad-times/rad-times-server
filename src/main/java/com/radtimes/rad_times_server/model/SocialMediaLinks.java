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
    @GeneratedValue
    private Integer id;

    private Integer person_id;
    private SocialType social_type;
    private String url_link;
}
