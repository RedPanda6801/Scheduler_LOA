package com.example.loa.Service;

import com.example.loa.Dto.CharacterInfoDto;
import com.example.loa.Entity.CharacterInfo;
import com.example.loa.Entity.User;
import com.example.loa.Repository.CharacterInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CharacterService {
    @Autowired
    private CharacterInfoRepository characterInfoRepository;
    @Autowired
    private UserService userService;
    public boolean init(Integer id, List<CharacterInfoDto> characterInfoDtoList){
        // user 정보 가져오기
        User user = userService.getById(id).get();

        // 각 캐릭터에 user 매핑
        List<CharacterInfo> characters = new ArrayList<>();

        for(CharacterInfoDto dto : characterInfoDtoList) {
            CharacterInfo character = CharacterInfo.toEntity(dto, user);
            characters.add(character);
        }

        // 매핑한 캐릭터 DB에 저장
        try{
            characterInfoRepository.saveAll(characters);
        }catch(Exception e){
            System.out.println(String.format("[Error] %s",e));
            return false;
        }
        return true;
    }

    public List<CharacterInfoDto> getCharacterByUserId(Integer id){

        List<CharacterInfoDto> characterInfoDtoList = new ArrayList<>();
        try{
            // id 값으로 찾기
            List<CharacterInfo> characterInfosTmp = characterInfoRepository.findAllByUserId(id);

            // entity를 Dto로 변환
            for(CharacterInfo entity : characterInfosTmp){
                CharacterInfoDto dto = CharacterInfoDto.toDto(entity);
                characterInfoDtoList.add(dto);
            }
        }catch(Exception e){
            System.out.println(String.format("[Error] %s",e));
            return null;
        }
        return characterInfoDtoList;
    }
}
