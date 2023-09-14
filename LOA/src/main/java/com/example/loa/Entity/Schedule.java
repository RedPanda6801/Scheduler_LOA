package com.example.loa.Entity;

import com.example.loa.Dto.ScheduleDto;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"characterinfo"})
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer valtan;

    private Integer biakiss;

    private Integer kuke;

    private Integer abrel;

    private Integer akkan;

    private Integer kkayangel;

    private Integer sanghatop;

    private Integer kamen;

    @OneToOne(mappedBy = "schedule")
    private CharacterInfo characterinfo;

    public static Schedule toEntity(ScheduleDto dto) {
        return Schedule.builder()
                .id(dto.getId())
                .valtan(dto.getValtan())
                .biakiss(dto.getBiakiss())
                .kuke(dto.getKuke())
                .abrel(dto.getAbrel())
                .akkan(dto.getAkkan())
                .kkayangel(dto.getKkayangel())
                .sanghatop(dto.getSanghatop())
                .kamen(dto.getKamen())
                .build();
    }

    public void update(ScheduleDto dto){
        if(dto.getValtan() != null) this.setValtan(dto.getValtan());
        if(dto.getBiakiss() != null) this.setBiakiss(dto.getBiakiss());
        if(dto.getKuke() != null) this.setKuke(dto.getKuke());
        if(dto.getAbrel() != null) this.setAbrel(dto.getAbrel());
        if(dto.getAkkan() != null) this.setAkkan(dto.getAkkan());
        if(dto.getKkayangel() != null) this.setKkayangel(dto.getKkayangel());
        if(dto.getSanghatop() != null) this.setSanghatop(dto.getSanghatop());
        if(dto.getKamen() != null) this.setKamen(dto.getKamen());
    }
}



