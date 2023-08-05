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

    private Boolean abrel_12;

    private Boolean abrel_34;

    private Boolean abrel_56;

    private Boolean akkan;

    private Boolean kkayangel;

    private Boolean sanghatop;

    private Boolean khamen;

    @OneToOne(mappedBy = "schedule")
    private Character_info character_info;
}
