package com.example.loa.Entity;

import com.example.loa.Dto.ScheduleDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    public static Schedule toEntity(ScheduleDto dto) {
        return Schedule.builder()
                .id(dto.getId())
                .valtan(dto.getValtan())
                .biakiss(dto.getBiakiss())
                .kuke(dto.getKuke())
                .abrel12(dto.getAbrel12())
                .abrel34(dto.getAbrel34())
                .abrel56(dto.getAbrel56())
                .akkan(dto.getAkkan())
                .kkayangel(dto.getKkayangel())
                .sanghatop(dto.getSanghatop())
                .khamen(dto.getKhamen())
                .build();
    }
}
