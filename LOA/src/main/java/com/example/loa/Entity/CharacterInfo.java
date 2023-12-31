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

    private String job;

    private Integer level;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user")
    private User user;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule", referencedColumnName = "id")
    private Schedule schedule;

    public static CharacterInfo toEntity(CharacterInfoDto dto, User user, Schedule schedule) {
        return CharacterInfo.builder()
                .charName(dto.getCharName())
                .level(dto.getLevel())
                .job(dto.getJob())
                .schedule(schedule)
                .user(user)
                .build();
    }

    public void update(String charName, Integer level, String job){
        if(charName != null) this.setCharName(charName);
        if(level != null) this.setLevel(level);
        if(job != null) this.setJob(job);
    }
}
