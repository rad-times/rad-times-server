package com.radtimes.rad_times_server.model.footage;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="clip_likes")
public class ClipLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clip_id;
    private Integer person_id;
}
