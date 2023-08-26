package com.example.loa.Dto;

import com.example.loa.Entity.CrewMember;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CrewMemberDto {
    private Integer id;

    private String user;

    private String crew;

    public static CrewMemberDto toDto(CrewMember entity) {
        return CrewMemberDto.builder()
                .id(entity.getId())
                .user(entity.getUser().getCharName())
                .crew(entity.getCrew().getName())
                .build();
    }
}


