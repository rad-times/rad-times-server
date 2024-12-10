package com.radtimes.rad_times_server.model.footage;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name="clip")
public class ClipModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "like_count")
    private String likeCount;

    @Column(name = "create_date")
    private Long createDate;

    private Integer location_id;
    private Integer person_id;
}
