package com.example.loa.Dto;

import com.example.loa.Entity.User;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDto {
    private Integer id;

    private String userId;

    private String password;

    private String charName;

    private String server;

    public static UserDto toDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .charName(entity.getCharName())
                .server(entity.getServer())
                .build();
    }
}
