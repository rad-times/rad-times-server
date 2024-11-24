package com.radtimes.rad_times_server.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="crew")
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer person_one;
    private Integer person_two;
    private Integer status;
}
