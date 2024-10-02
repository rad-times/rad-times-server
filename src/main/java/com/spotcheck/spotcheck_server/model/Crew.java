package com.spotcheck.spotcheck_server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="crew")
public class Crew {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer person_one;
    private Integer person_two;
    private Integer status;
}
