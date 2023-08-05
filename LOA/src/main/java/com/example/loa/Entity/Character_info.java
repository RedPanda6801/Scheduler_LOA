package com.example.loa.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Character_info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String char_name;

    private Integer level;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule", referencedColumnName = "id")
    private Schedule schedule;
}
