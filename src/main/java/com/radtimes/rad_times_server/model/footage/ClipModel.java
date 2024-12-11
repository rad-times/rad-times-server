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

    @Column(name = "s3_file_name")
    private String s3FileName;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "create_date")
    private Long createDate;

    private Integer location_id;
    private Integer person_id;
}
