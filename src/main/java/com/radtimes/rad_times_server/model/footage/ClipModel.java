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

    private String image_url;
    private Integer like_count;
    private Date create_date;

    private Integer location_id;
    private Integer person_id;
}
