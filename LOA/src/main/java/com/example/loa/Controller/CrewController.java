package com.example.loa.Controller;

import com.example.loa.Service.CrewService;
import com.example.loa.Service.JWTService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String addMember(HttpServletRequest request, @RequestBody String crewName){
        // 유저 정보 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return null;

        Integer id = Integer.parseInt(token.get("id").toString());
        Boolean isAdder = crewService.addMember(id, crewName);
        if(!isAdder) return "Add Failed";
        return "Add Success";
    }
}
