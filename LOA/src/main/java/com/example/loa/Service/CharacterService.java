package com.example.loa.Service;

import com.example.loa.Dto.CharacterInfoDto;
import com.example.loa.Entity.CharacterInfo;
import com.example.loa.Entity.User;
import com.example.loa.Repository.CharacterInfoRepository;
import com.example.loa.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public boolean updateCharacters(Integer id, List<CharacterInfoDto> characters){
        // 캐릭터에 매핑할 유저 조회
        Optional<User> userOptional = userService.getById(id);
        if(!userOptional.isPresent()) {
            System.out.println("[Error] No User Error");
            return false;
        }
        List<CharacterInfoDto> originCharacters = getCharacterByUserId(id);
        // 갱신할 캐릭터들 List 생성
        List<String> addCharNames = new ArrayList<>();
        for(CharacterInfoDto dto : characters){
            addCharNames.add(dto.getCharName());
        }

        // 체크되지 않는 캐릭터 삭제
        try{
            List<Integer> deleteIds = new ArrayList<>();
            for(CharacterInfoDto dto : originCharacters){
                String originName = dto.getCharName();
                if (!addCharNames.contains(originName)) {
                    deleteIds.add(dto.getId());
                }else{
                    // 기존에 있는 이름은 추가할 배열에서 삭제
                    addCharNames.remove(originName);
                }
            }
            // 제외된 데이터 삭제 및 예외처리
            if(deleteIds.size() > 0) {
                characterInfoRepository.deleteAllById(deleteIds);
            }else{
                // 추가할 데이터가 없으면 알림을 보내고 성공처리
                System.out.println("[Alert] No data Changed");
                return true;
            }
            // 추가된 캐릭터들의 Dto를 Entity로 변환
            List<CharacterInfo> addCharacters = new ArrayList<>();
            for(CharacterInfoDto character : characters){
                if(addCharNames.contains(character.getCharName())){
                    addCharacters.add(CharacterInfo.toEntity(character, userOptional.get()));
                }
            }
            // 추가할 캐릭터를 DB에 추가
            if(addCharacters.size() > 0) {
                characterInfoRepository.saveAll(addCharacters);
                System.out.println("[Alert] Update Success");
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            return false;
        }
    }

}
