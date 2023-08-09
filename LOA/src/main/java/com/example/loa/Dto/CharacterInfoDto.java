package com.example.loa.Dto;

import com.example.loa.Entity.CharacterInfo;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}
