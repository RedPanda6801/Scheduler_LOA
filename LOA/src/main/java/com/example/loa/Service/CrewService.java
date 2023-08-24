package com.example.loa.Service;

import com.example.loa.Entity.Crew;
import com.example.loa.Entity.User;
import com.example.loa.Repository.CrewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CrewService {

    @Autowired
    CrewRepository crewRepository;

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
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            return false;
        }
        return true;
    }
}
