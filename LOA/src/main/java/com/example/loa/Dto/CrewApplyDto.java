package com.example.loa.Dto;

import lombok.*;

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
}
