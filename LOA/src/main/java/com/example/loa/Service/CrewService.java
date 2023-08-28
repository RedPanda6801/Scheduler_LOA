package com.example.loa.Service;

import com.example.loa.Dto.CharacterInfoDto;
import com.example.loa.Dto.CrewMemberDto;
import com.example.loa.Entity.Crew;
import com.example.loa.Entity.CrewMember;
import com.example.loa.Entity.User;
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

    public Boolean addMember(Integer id, String crewName){
        // 추가할 계정 불러오기
        Optional<User> userTmp = userService.getById(id);
        if(!userTmp.isPresent()){
            System.out.println("[Error] No User Existed");
            return false;
        }
        User crewOne = userTmp.get();
        // 크루 가져오기
        Optional<Crew> crewTmp = crewRepository.findCrewByName(crewName);
        if(!crewTmp.isPresent()){
            System.out.println("[Error] No Crew Existed");
            return false;
        }
        Crew crew = crewTmp.get();

        // 크루 맴버 DB 추가
        try{
            CrewMember adder = new CrewMember();
            adder.setUser(crewOne);
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
