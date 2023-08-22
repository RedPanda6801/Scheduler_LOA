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

    @OneToOne(mappedBy = "schedule")
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

    public void update(ScheduleDto dto){
        if(dto.getValtan() != null) this.setValtan(dto.getValtan());
        if(dto.getBiakiss() != null) this.setBiakiss(dto.getBiakiss());
        if(dto.getKuke() != null) this.setKuke(dto.getKuke());
        if(dto.getAbrel12() != null) this.setAbrel12(dto.getAbrel12());
        if(dto.getAbrel34() != null) this.setAbrel34(dto.getAbrel34());
        if(dto.getAbrel56() != null) this.setAbrel56(dto.getAbrel56());
        if(dto.getAkkan() != null) this.setAkkan(dto.getAkkan());
        if(dto.getKkayangel() != null) this.setKkayangel(dto.getKkayangel());
        if(dto.getSanghatop() != null) this.setSanghatop(dto.getSanghatop());
        if(dto.getKhamen() != null) this.setKhamen(dto.getKhamen());
    }
}



