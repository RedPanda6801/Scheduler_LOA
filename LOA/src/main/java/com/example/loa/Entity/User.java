package com.example.loa.Entity;

import com.example.loa.Dto.UserDto;
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
@ToString(exclude = {"characterInfos", "crews", "userCrews", "applies", "boards"})
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    List<CharacterInfo> characterInfos = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Crew> crews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<UserCrew> userCrews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    List<Apply> applies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
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
        this.setPassword(password);
        this.setCharName(charName);
        this.setServer(server);
    }
}
