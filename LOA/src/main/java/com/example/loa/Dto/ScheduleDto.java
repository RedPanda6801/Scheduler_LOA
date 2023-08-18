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

    private Integer charId;

    public static ScheduleDto toDto(Schedule entity) {
        return ScheduleDto.builder()
                .id(entity.getId())
                .valtan(entity.getValtan())
                .biakiss(entity.getBiakiss())
                .kuke(entity.getKuke())
                .abrel12(entity.getAbrel12())
                .abrel34(entity.getAbrel34())
                .abrel56(entity.getAbrel56())
                .akkan(entity.getAkkan())
                .kkayangel(entity.getKkayangel())
                .sanghatop(entity.getSanghatop())
                .khamen(entity.getKhamen())
                .build();
    }
}
