package com.example.loa.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String user_id;

    private String password;

    private String char_name;

    private String server;

    @OneToMany(mappedBy = "user")
    List<Character_info> users = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Crew> crews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<User_Crew> user_crews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Apply> applies = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Board> boards = new ArrayList<>();

}
