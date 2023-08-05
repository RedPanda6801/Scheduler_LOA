package com.example.loa.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String context;

    private String roasting; // 헤딩, 트라이, 클경, 반숙, 숙련, 숙제

    private Integer level;

    private String privacy; // 전체공개, 크루공개

    private String card;

    private Integer max_member;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @OneToMany(mappedBy = "board")
    List<Hire> hires = new ArrayList<>();

}
