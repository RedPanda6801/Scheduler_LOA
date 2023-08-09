package com.example.loa.Entity;

import com.example.loa.Dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="user_id", nullable = false)
    private String userId;

    private String password;

    @Column(name="char_name", nullable = false)
    private String charName;

    private String server;

    @OneToMany(mappedBy = "user")
    List<CharacterInfo> users = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Crew> crews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<UserCrew> userCrews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Apply> applies = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Board> boards = new ArrayList<>();

    public static User toEntity(UserDto dto) {
        return User.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .charName(dto.getCharName())
                .server(dto.getServer())
                .build();
    }

    public void update(String password, String charName, String server){
        this.password = password;
        this.charName = charName;
        this.server = server;
    }
}
