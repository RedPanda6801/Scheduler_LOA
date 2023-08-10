package com.example.loa.Entity;

import com.example.loa.Dto.CharacterInfoDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="char_name", nullable = false)
    private String charName;

    private Integer level;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule", referencedColumnName = "id")
    private Schedule schedule;

    public static CharacterInfo toEntity(CharacterInfoDto dto, User user) {
        return CharacterInfo.builder()
                .charName(dto.getCharName())
                .level(dto.getLevel())
                .user(user)
                .build();
    }
}
