package com.radtimes.rad_times_server.model.footage;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="clip_comment")
public class ClipComment {
    private enum CommentStatus {
        ACTIVE, DELETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;
    private Date create_date;
    private CommentStatus status;

    private Integer author_id;
    private Long clip_id;

}
