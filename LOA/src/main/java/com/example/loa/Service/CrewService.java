package com.example.loa.Service;

import com.example.loa.Entity.Crew;
import com.example.loa.Entity.CrewMember;
import com.example.loa.Entity.User;
import com.example.loa.Repository.CrewMemberRepository;
import com.example.loa.Repository.CrewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CrewService {

    @Autowired
    CrewRepository crewRepository;

    @Autowired
    CrewMemberRepository crewMemberRepository;

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
}
