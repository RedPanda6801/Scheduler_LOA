package com.example.loa.Dto;

import com.example.loa.Entity.Schedule;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ScheduleDto {
    private Integer id;

    private Integer valtan;

    private Integer biakiss;

    private Integer kuke;

    private Integer abrel;

    private Integer akkan;

    private Integer kkayangel;

    private Integer sanghatop;

    private Integer kamen;

    private Integer userId;

    public static ScheduleDto toDto(Schedule entity) {
        return ScheduleDto.builder()
                .id(entity.getId())
                .valtan(entity.getValtan())
                .biakiss(entity.getBiakiss())
                .kuke(entity.getKuke())
                .abrel(entity.getAbrel())
                .akkan(entity.getAkkan())
                .kkayangel(entity.getKkayangel())
                .sanghatop(entity.getSanghatop())
                .kamen(entity.getKamen())
                .build();
    }
}
