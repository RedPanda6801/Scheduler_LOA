package com.example.loa.Entity;

import com.example.loa.Dto.CrewDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 출력 시에 객체 무한 참조를 막기 위한 exclude
@ToString(exclude = {"userCrews","user"})
public class Crew {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="head")
    private User user;

    @OneToMany(mappedBy = "crew")
    List<CrewMember> crewMembers = new ArrayList<>();

    @OneToMany(mappedBy = "crew")
    List<CrewApply> crewApplies = new ArrayList<>();

    public static Crew toEntity(CrewDto dto, User user){
        return Crew.builder()
                .name(dto.getName())
                .user(user)
                .build();
    }
}
