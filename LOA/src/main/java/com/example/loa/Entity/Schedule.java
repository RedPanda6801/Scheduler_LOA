package com.example.loa.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Boolean valtan;

    private Boolean biakiss;

    private Boolean kuke;

    private Boolean abrel12;

    private Boolean abrel34;

    private Boolean abrel56;

    private Boolean akkan;

    private Boolean kkayangel;

    private Boolean sanghatop;

    private Boolean khamen;

    @OneToOne(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private CharacterInfo characterinfo;
}
