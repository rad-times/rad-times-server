package com.spotcheck.spotcheck_server.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name="keyword")
public class Keyword {
    @Id
    @GeneratedValue
    private Integer keyword_id;

    private String keyword_name;
    private String keyword_description;
}
