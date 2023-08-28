package com.example.loa.Dto;

import com.example.loa.Entity.CharacterInfo;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CharacterInfoDto {
    private Integer id;

    private String charName;

    private Integer level;

    private String userId;

    private String job;

    private ScheduleDto scheduleDto;

    private String mainCharacter;

    public static CharacterInfoDto toDto(CharacterInfo entity) {
        return CharacterInfoDto.builder()
                .id(entity.getId())
                .charName(entity.getCharName())
                .level(entity.getLevel())
                .job(entity.getJob())
                .userId(String.valueOf(entity.getUser().getId()))
                .scheduleDto(ScheduleDto.toDto(entity.getSchedule()))
                .build();
    }
}
