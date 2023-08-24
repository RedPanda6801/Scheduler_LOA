package com.example.loa.Service;

import com.example.loa.Dto.CharacterInfoDto;
import com.example.loa.Entity.CharacterInfo;
import com.example.loa.Entity.Schedule;
import com.example.loa.Entity.User;
import com.example.loa.Repository.CharacterInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CharacterService {
    @Autowired
    private CharacterInfoRepository characterInfoRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ScheduleService scheduleService;
    public boolean init(Integer id, List<CharacterInfoDto> characterInfoDtoList){
        // user 정보 가져오기
        Optional<User> userTmp = userService.getById(id);
        if(!userTmp.isPresent()) {
            System.out.println("[Error] No User Existed");
            return false;
        }
        User user = userTmp.get();

        // 각 캐릭터에 user 매핑
        List<CharacterInfo> characters = new ArrayList<>();
        try{
            for(CharacterInfoDto dto : characterInfoDtoList) {
                // dto 내용 검사
                if(dto.getLevel() == null || dto.getJob() == null || dto.getCharName() == null){
                    System.out.println(String.format("[Error] Data Empty Error"));
                    return false;
                }
                Schedule schedule = scheduleService.init();
                CharacterInfo character = CharacterInfo.toEntity(dto, user, schedule);
                characters.add(character);
            }
        }catch(Exception e){
            System.out.println(String.format("[Error] %s",e));
            return false;
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

    // 개인 케릭터 및 스케줄 조회
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

    public boolean changeCharacters(Integer id, List<CharacterInfoDto> characters){
        // 캐릭터에 매핑할 유저 조회
        Optional<User> userOptional = userService.getById(id);
        if(!userOptional.isPresent()) {
            System.out.println("[Error] No User Existed");
            return false;
        }
        List<CharacterInfoDto> originCharacters = getCharacterByUserId(id);
        // 갱신할 캐릭터들 List 생성
        List<String> addCharNames = new ArrayList<>();
        for(CharacterInfoDto dto : characters){
            // 부족한 정보에 대한 예외처리
            if(dto.getCharName() == null || dto.getJob() == null || dto.getLevel() == null){
                System.out.println("[Error] Data Empty Error");
                return false;
            }
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
                    Schedule schedule = scheduleService.init();
                    addCharacters.add(CharacterInfo.toEntity(character, userOptional.get(), schedule));
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
    // 캐릭터 단일 수정
    @Transactional
    public Boolean modifyCharacter(CharacterInfoDto dto){
        // 기존 캐릭터 정보 불러오기
        Optional<CharacterInfo> characterTmp = characterInfoRepository.findById(dto.getId());
        if(!characterTmp.isPresent()){
            System.out.println("[Error] Find Character Error");
            return false;
        }
        CharacterInfo character = characterTmp.get();

        // 값 수정
        try{
            Integer level = dto.getLevel();
            String name = dto.getCharName();
            String job = dto.getJob();
            character.update(name, level, job);
            return true;
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            return false;
        }
    }

    @Transactional
    public Boolean updateCharacters(List<CharacterInfoDto> dtos){
        // 배열 선언
        List<CharacterInfo> charArr = new ArrayList<>();
        List<CharacterInfoDto> changeArr = new ArrayList<>();
        for(CharacterInfoDto dto : dtos){
            // 형식에 맞지 않는 dto에 대한 예외처리
            if(dto.getCharName() == null || dto.getLevel() == null || dto.getJob() == null){
                System.out.println(String.format("[Error] Data Empty Error"));
            }
            // 현재 캐릭터들의 정보 찾기
            Optional<CharacterInfo> charTmp = characterInfoRepository.findById(dto.getId());
            if(!charTmp.isPresent()){
                System.out.println(String.format("[Alert] No Character : %s", dto.getCharName()));
            }else{
                CharacterInfo charElement = charTmp.get();
                // 값이 변하지 않은 것은 수정 X
                if(!charElement.getCharName().equals(dto.getCharName()) || charElement.getLevel() != dto.getLevel()){
                    charArr.add(charElement);
                    changeArr.add(dto);
                }
            }
        }
        try{
            // 필터된 엔티티들을 update
            for(int i = 0; i < charArr.size(); i++){
                CharacterInfoDto newObject = changeArr.get(i);
                charArr.get(i).update(newObject.getCharName(), newObject.getLevel(), newObject.getJob());
            }
            return true;
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            return false;
        }
    }

}
