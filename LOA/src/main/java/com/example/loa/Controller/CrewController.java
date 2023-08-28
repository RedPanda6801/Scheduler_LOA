package com.example.loa.Controller;

import com.example.loa.Dto.CharacterInfoDto;
import com.example.loa.Dto.CrewMemberDto;
import com.example.loa.Service.CrewService;
import com.example.loa.Service.JWTService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CrewController {

    @Autowired
    CrewService crewService;

    JWTService jwtService = new JWTService();

    // 새 크루 생성
    @PostMapping("/api/crew/add-crew")
    @ResponseBody
    public String addCrew(HttpServletRequest request, @RequestBody String crewName){
        // 유저 정보 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return null;

        Integer id = Integer.parseInt(token.get("id").toString());
        Boolean isCreated = crewService.addCrew(id, crewName);
        if(!isCreated) return "Create Failed";
        return "Create Success";
    }

    // 크루 인원 추가
    @PostMapping("/api/crew/add-member")
    @ResponseBody
    public String addMember(HttpServletRequest request, @RequestBody CrewMemberDto dto){
        // 유저 정보 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return null;

        Integer id = Integer.parseInt(token.get("id").toString());
        Boolean isAdder = crewService.addMember(id, dto);
        if(!isAdder) return "Add Failed";
        return "Add Success";
    }

    // 내 크루 검색
    @GetMapping("/api/crew/get-crew")
    @ResponseBody
    public List<String> getMyCrew(HttpServletRequest request){
        // 유저 정보 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return null;

        Integer id = Integer.parseInt(token.get("id").toString());
        List<String> result = crewService.getCrew(id);
        if(result.size() == 0) System.out.println("[Alert] Data is Null");
        return result;
    }

    // 크루 멤버 검색
    @GetMapping("/api/crew/get-members/{crew}")
    @ResponseBody
    public List<List<CharacterInfoDto>> getCrewMembers(HttpServletRequest request, @PathVariable String crew){
        // 유저 정보 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return null;

        List<List<CharacterInfoDto>> users = crewService.getCrewMembers(crew);
        if(users.size() == 0) System.out.println("[Alert] Data is Null");
        return users;
    }
}
