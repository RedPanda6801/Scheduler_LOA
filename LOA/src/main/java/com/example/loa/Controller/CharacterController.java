package com.example.loa.Controller;

import com.example.loa.Dto.CharacterInfoDto;
import com.example.loa.Service.CharacterService;
import com.example.loa.Service.JWTService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    private JWTService jwtService = new JWTService();
    @PostMapping("/api/character/init")
    @ResponseBody
    public String initChar(HttpServletRequest request, @RequestBody List<CharacterInfoDto> characterInfoDtoList){

        // 로그인 정보 확인
        Claims token = jwtService.checkAuthorizationHeader(request);
        if(token == null){
            System.out.println("[Error] Token is Missed");
            return null;
        }
        // 유저 확인
        String id = token.get("id").toString();

        try{
            characterService.init(Integer.parseInt(id), characterInfoDtoList);
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            return "Init Failed";
        }
        return "Init Success";
    }
}
