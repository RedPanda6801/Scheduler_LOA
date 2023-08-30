package com.example.loa.Dto;

import com.example.loa.Entity.CrewApply;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CrewApplyDto {
    private Integer id;

    private String crewName;

    private String userId;

    private String charName;

    private String details;

    public static CrewApplyDto toDto(CrewApply entity){
        return CrewApplyDto.builder()
                .id(entity.getId())
                .crewName(entity.getCrew().getName())
                .charName(entity.getUser().getCharName())
                .details(entity.getDetails())
                .userId(entity.getUser().getUserId())
                .build();
    }
}
