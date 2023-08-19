package com.example.loa.Entity;

import com.example.loa.Dto.CharacterInfoDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user", "schedule"})
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

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "schedule", referencedColumnName = "id")
    private Schedule schedule;

    public static CharacterInfo toEntity(CharacterInfoDto dto, User user, Schedule schedule) {
        return CharacterInfo.builder()
                .charName(dto.getCharName())
                .level(dto.getLevel())
                .schedule(schedule)
                .user(user)
                .build();
    }

    public void update(String charName, Integer level){
        this.setCharName(charName);
        this.setLevel(level);
    }
}
