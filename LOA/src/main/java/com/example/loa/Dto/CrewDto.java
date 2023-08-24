package com.example.loa.Dto;

import com.example.loa.Entity.Crew;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CrewDto {
    private Integer id;

    private String name;

    private String head;

    public static CrewDto toDto(Crew entity){
        return CrewDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .head(String.valueOf(entity.getUser().getId()))
                .build();
    }
}
