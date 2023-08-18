package com.example.loa.Dto;

import com.example.loa.Entity.CharacterInfo;
import lombok.*;

import java.util.List;

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

    private String scheduleId;

    public static CharacterInfoDto toDto(CharacterInfo entity) {
        return CharacterInfoDto.builder()
                .id(entity.getId())
                .charName(entity.getCharName())
                .level(entity.getLevel())
                .userId(String.valueOf(entity.getUser().getId()))
                .build();
    }
}
