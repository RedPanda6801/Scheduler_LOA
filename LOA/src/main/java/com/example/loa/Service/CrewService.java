package com.example.loa.Service;

import com.example.loa.Dto.CharacterInfoDto;
import com.example.loa.Dto.CrewApplyDto;
import com.example.loa.Dto.CrewMemberDto;
import com.example.loa.Entity.Crew;
import com.example.loa.Entity.CrewApply;
import com.example.loa.Entity.CrewMember;
import com.example.loa.Entity.User;
import com.example.loa.Repository.CrewApplyRepository;
import com.example.loa.Repository.CrewMemberRepository;
import com.example.loa.Repository.CrewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CrewService {

    @Autowired
    CrewRepository crewRepository;

    @Autowired
    CrewMemberRepository crewMemberRepository;

    @Autowired
    CrewApplyRepository crewApplyRepository;

    @Autowired
    CharacterService characterService;

    @Autowired
    UserService userService;

    public Boolean addCrew(Integer id, String crewName){
        // 유저 정보 찾기
        Optional<User> userTmp = userService.getById(id);
        if(!userTmp.isPresent()){
            System.out.println("[Error] No User Existed");
            return false;
        }

        User user = userTmp.get();
        // 캐릭터 정보 저장
        try{
            Crew crew = new Crew();
            crew.setName(crewName);
            crew.setUser(user);
            crewRepository.save(crew);
            // 크루 멤버에 인원 추가
            CrewMember adder = new CrewMember();
            adder.setCrew(crew);
            adder.setUser(user);
            crewMemberRepository.save(adder);
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            return false;
        }
        return true;
    }

    public Boolean applyCrew(CrewApplyDto dto){
        // 유저 확인
        User user = userService.getByUserId(dto.getUserId());
        // 입력한 대표캐릭터와 유저의 대표캐릭터가 일치하지 않으면 예외처리
        if(!user.getCharName().equals(dto.getCharName())){
            System.out.println("[Error] Data is Not Matched");
            return false;
        }
        // 크루 확인
        Optional<Crew> crewTmp = crewRepository.findCrewByName(dto.getCrewName());
        if(!crewTmp.isPresent()){
            System.out.println("[Error] No Crew Existed");
            return false;
        }
        Crew crew = crewTmp.get();
        // 신청서 저장
        try{
            CrewApply apply = new CrewApply();
            apply.setUser(user);
            apply.setDetails(dto.getDetails());
            apply.setCrew(crew);
            crewApplyRepository.save(apply);
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            return false;
        }
        return true;
    }

    public List<CrewApplyDto> getCrewApplies(Integer id, String crewName){
        // 요청 크루 조회
        Optional<Crew> crewTmp = crewRepository.findCrewByName(crewName);
        if(!crewTmp.isPresent()){
            System.out.println("[Error] No Crew Existed");
            return null;
        }
        Crew crew = crewTmp.get();
        // crew 헤드의 DB id값과 JWT 내의 id 값을 비교
        if(crew.getUser().getId() != id){
            System.out.println("[Error] Forbidden Error");
            return null;
        }

        List<CrewApplyDto> results = new ArrayList<>();
        try{
            // 크루로 지원서 조회
            List<CrewApply> crewApplies = crewApplyRepository.findAllByCrew(crew);
            // DB 값을 DTO로 변환
            for(CrewApply crewApply : crewApplies){
                CrewApplyDto dto = CrewApplyDto.toDto(crewApply);
                results.add(dto);
            }
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            return null;
        }
        return results;
    }
    // dto 정보 : crew 이름, 크루원의 userId
    public Boolean addMember(Integer id, CrewMemberDto dto){
        // 내 권한 불러오기
        Optional<User> userTmp = userService.getById(id);
        if(!userTmp.isPresent()){
            System.out.println("[Error] No User Existed");
            return false;
        }
        User head = userTmp.get();
        // 크루 가져오기
        Optional<Crew> crewTmp = crewRepository.findCrewByName(dto.getCrew());
        if(!crewTmp.isPresent()){
            System.out.println("[Error] No Crew Existed");
            return false;
        }
        Crew crew = crewTmp.get();
        // 크루 헤드가 아니면 추가 못하게 함
        if(crew.getUser().getId() != head.getId()){
            System.out.println("[Error] Forbidden Error");
            return false;
        }
        // 추가할 유저 찾기
        User addUser = userService.getByUserId(dto.getUser());
        // 크루 맴버 DB 추가
        try{
            CrewMember adder = new CrewMember();
            adder.setUser(addUser);
            adder.setCrew(crew);
            crewMemberRepository.save(adder);
        }catch (Exception e){
            System.out.println(String.format("[Error] %s", e));
            return false;
        }

        return true;
    }

    public List<String> getCrew(Integer id){
        Optional<User> userTmp = userService.getById(id);
        if(!userTmp.isPresent()){
            System.out.println("[Alert] No User Existed");
            return null;
        }
        User user = userTmp.get();

        List<String> results = new ArrayList<>();
        try{
            // 유저 정보로 크루 가져오기
            List<CrewMember> crews = crewMemberRepository.findAllByUser(user);
            // Dto로 변환
            for(CrewMember crew : crews){
                CrewMemberDto dto = CrewMemberDto.toDto(crew);
                results.add(dto.getCrew());
            }
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            return null;
        }
        return results;
    }

    public List<List<CharacterInfoDto>> getCrewMembers(String crewName){
        // 크루 id로 crew 가져오기
        Optional<Crew> crewTmp = crewRepository.findCrewByName(crewName);
        if(!crewTmp.isPresent()){
            System.out.println("[Error] No Crew Existed");
            return null;
        }

        Crew crew = crewTmp.get();
        // 크루 멤버 목록 가져오기
        List<CrewMember> members = crewMemberRepository.findAllByCrew(crew);
        if(members.size() == 0) {
            System.out.println("[Error] No Members Existed");
            return null;
        }
        // 크루 멤버에서 얻은 유저로 CharacterInfo 가져오기
        List<List<CharacterInfoDto>> results = new ArrayList<>();

        try{
            for(CrewMember member : members){
                User user = member.getUser();
                List<CharacterInfoDto> characters = characterService.getCharacterByUserId(user.getId(), user.getCharName());
                results.add(characters);
            }
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            return null;
        }
        return results;
    }
}
