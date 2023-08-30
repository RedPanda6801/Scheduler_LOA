package com.example.loa.Controller;

import com.example.loa.Dto.CharacterInfoDto;
import com.example.loa.Dto.CrewApplyDto;
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

    // 크루 지원서 작성
    @PostMapping("/api/crew/apply-crew")
    @ResponseBody
    public String applyCrew(HttpServletRequest request, @RequestBody CrewApplyDto applyDto){
        // 유저 정보 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return null;

        Boolean isApplied = crewService.applyCrew(applyDto);
        if(!isApplied) return "Apply Failed";
        return "Apply Success";
    }

    // 크루 지원서 조회
    @GetMapping("/api/crew/get-applies/{crewName}")
    @ResponseBody
    public List<CrewApplyDto> getCrewApplies(HttpServletRequest request, @PathVariable String crewName){
        // 유저 정보 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return null;

        Integer id = Integer.parseInt(token.get("id").toString());
        List<CrewApplyDto> applies = crewService.getCrewApplies(id, crewName);
        if(applies == null) System.out.println("[Alert] No Applies In Crew");
        return applies;

    }

    // 크루 지원서 반려
    @PostMapping("/api/crew/delete-applies/{applyId}")
    @ResponseBody
    public String deleteCrewApply(HttpServletRequest request, @PathVariable String applyId){
        // 유저 정보 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return null;

        Integer id = Integer.parseInt(token.get("id").toString());
        Boolean isDeleted = crewService.deleteCrewApply(id, applyId);
        if(!isDeleted) return "Delete Failed";
        return "Delete Success";
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
