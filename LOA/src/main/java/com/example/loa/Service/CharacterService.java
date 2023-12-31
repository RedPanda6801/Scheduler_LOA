package com.example.loa.Service;

import com.example.loa.Dto.CharacterInfoDto;
import com.example.loa.Dto.ResponseDto;
import com.example.loa.Entity.CharacterInfo;
import com.example.loa.Entity.Schedule;
import com.example.loa.Entity.User;
import com.example.loa.Repository.CharacterInfoRepository;
import com.example.loa.Repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CharacterService {
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    private CharacterInfoRepository characterInfoRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ScheduleService scheduleService;
    public ResponseDto init(Integer id, List<CharacterInfoDto> characterInfoDtoList){
        // 응답값 객체 생성
        ResponseDto response = new ResponseDto();
        // user 정보 가져오기
        Optional<User> userTmp = userService.getById(id);
        if(!userTmp.isPresent()) {
            System.out.println("[Error] No User Existed");
            response.setResponse("Not Existed", HttpStatus.BAD_REQUEST);
            return response;
        }
        User user = userTmp.get();

        // 기존에 가지고 있는 캐릭터가 있으면 전부 삭제
        try{
            List<CharacterInfo> trashChar = characterInfoRepository.findAllByUserId(id);
            // 배열에 캐릭터가 검색되어 있으면 삭제
            if (trashChar.size() > 0) {
                for (CharacterInfo character : trashChar) {
                    // 캐릭터에 연결된 스케줄을 찾아 삭제
                    Schedule deleteData = character.getSchedule();
                    // 연관관계 삭제
                    character.setSchedule(null);
                    // 데이터 삭제
                    scheduleRepository.delete(deleteData);
                    characterInfoRepository.delete(character);
                }
            }
        }catch(Exception e){
            System.out.println(String.format("[Error] %s",e));
            response.setResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }

        // 각 캐릭터에 user 매핑
        List<CharacterInfo> characters = new ArrayList<>();
        try{
            for(CharacterInfoDto dto : characterInfoDtoList) {
                // dto 내용 검사
                if(dto.getLevel() == null || dto.getJob() == null || dto.getCharName() == null){
                    System.out.println(String.format("[Error] Data Empty Error"));
                    response.setResponse("No Data", HttpStatus.BAD_REQUEST);
                    return response;
                }
                // 캐릭터의 스케줄 생성
                Schedule schedule = scheduleService.init();
                CharacterInfo character = CharacterInfo.toEntity(dto, user, schedule);
                characters.add(character);
            }
        }catch(Exception e){
            System.out.println(String.format("[Error] %s",e));
            response.setResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }

        // 매핑한 캐릭터 DB에 저장
        try{
            characterInfoRepository.saveAll(characters);
        }catch(Exception e){
            System.out.println(String.format("[Error] %s",e));
            response.setResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
        response.setResponse("Init Success", HttpStatus.CREATED);
        return response;
    }

    // 개인 케릭터 및 스케줄 조회
    public List<CharacterInfoDto> getCharacterByUserId(Integer id, String charName){

        List<CharacterInfoDto> characterInfoDtoList = new ArrayList<>();
        try{
            // id 값으로 찾기
            List<CharacterInfo> characterInfosTmp = characterInfoRepository.findAllByUserId(id);
            // entity를 Dto로 변환
            for(CharacterInfo entity : characterInfosTmp){
                CharacterInfoDto dto = CharacterInfoDto.toDto(entity);
                dto.setMainCharacter(charName);
                characterInfoDtoList.add(dto);
            }
        }catch(Exception e){
            System.out.println(String.format("[Error] %s",e));
            return null;
        }
        return characterInfoDtoList;
    }

    // 검색 방법 수정 필요
    public ResponseDto changeCharacters(Integer id, List<CharacterInfoDto> characters){
        // 응답값 객체 생성
        ResponseDto response = new ResponseDto();

        // 캐릭터에 매핑할 유저 조회
        Optional<User> userOptional = userService.getById(id);
        if(!userOptional.isPresent()) {
            System.out.println("[Error] No User Existed");
            response.setResponse("Not Existed", HttpStatus.BAD_REQUEST);
            return response;
        }
        User user = userOptional.get();
        // 기존 캐릭터들을 조회
        List<CharacterInfoDto> originCharacters = getCharacterByUserId(id, user.getCharName());
        // 갱신할 캐릭터들 List 생성
        List<String> addCharNames = new ArrayList<>();
        for(CharacterInfoDto dto : characters){
            // 부족한 정보에 대한 예외처리
            if(dto.getCharName() == null || dto.getJob() == null || dto.getLevel() == null){
                System.out.println("[Error] Data Empty Error");
                response.setResponse("No Data Error", HttpStatus.BAD_REQUEST);
                return response;
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
                response.setResponse("No Data Changed", HttpStatus.OK);
                return response;
            }
            // 추가된 캐릭터들의 Dto를 Entity로 변환
            List<CharacterInfo> addCharacters = new ArrayList<>();
            for(CharacterInfoDto character : characters){
                if(addCharNames.contains(character.getCharName())){
                    Schedule schedule = scheduleService.init();
                    addCharacters.add(CharacterInfo.toEntity(character, user, schedule));
                }
            }
            // 추가할 캐릭터를 DB에 추가
            if(addCharacters.size() > 0) {
                characterInfoRepository.saveAll(addCharacters);
                System.out.println("[Alert] Update Success");
                response.setResponse("Update Success", HttpStatus.OK);
                return response;
            }else{
                // 추가할 캐릭터 배열이 비어있으면 예외처리
                response.setResponse("No Data Changed", HttpStatus.BAD_REQUEST);
                return response;
            }
        }catch(Exception e){
            System.out.println(String.format("[Error] %s",e));
            response.setResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
    }
    // 캐릭터 단일 수정
    @Transactional
    public ResponseDto modifyCharacter(CharacterInfoDto dto){
        // 응답값 객체 생성
        ResponseDto response = new ResponseDto();

        // 기존 캐릭터 정보 불러오기
        Optional<CharacterInfo> characterTmp = characterInfoRepository.findById(dto.getId());
        if(!characterTmp.isPresent()){
            System.out.println("[Error] Find Character Error");
            response.setResponse("Not Existed", HttpStatus.BAD_REQUEST);
            return response;
        }
        CharacterInfo character = characterTmp.get();

        // 값 수정
        try{
            Integer level = dto.getLevel();
            String name = dto.getCharName();
            String job = dto.getJob();
            character.update(name, level, job);
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            response.setResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
        response.setResponse("Update Success", HttpStatus.OK);
        return response;
    }

    @Transactional
    public ResponseDto updateCharacters(List<CharacterInfoDto> dtos){
        // 응답값 객체 선언
        ResponseDto response = new ResponseDto();

        // 배열 선언
        List<CharacterInfo> charArr = new ArrayList<>();
        List<CharacterInfoDto> changeArr = new ArrayList<>();

        for(CharacterInfoDto dto : dtos){
            // 수정값이 없는 dto는 넘기기
            if(dto.getCharName() == null || dto.getLevel() == null || dto.getJob() == null){
                continue;
            }
            // 현재 캐릭터들의 정보 찾기
            Optional<CharacterInfo> charTmp = characterInfoRepository.findById(dto.getId());
            // 캐릭터를 찾지 못했으면 예외처리
            if(!charTmp.isPresent()){
                System.out.println(String.format("[Alert] No Character : %s", dto.getCharName()));
                response.setResponse("Not Existed", HttpStatus.BAD_REQUEST);
                return response;
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
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            response.setResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
        response.setResponse("Update Success", HttpStatus.OK);
        return response;
    }

}
