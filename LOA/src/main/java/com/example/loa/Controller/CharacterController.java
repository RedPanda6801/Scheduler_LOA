package com.example.loa.Controller;

import com.example.loa.Dto.CharacterInfoDto;
import com.example.loa.Repository.UserRepository;
import com.example.loa.Service.CharacterService;
import com.example.loa.Service.JWTService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @Autowired
    private UserRepository userRepository;

    private Claims jwtCheckFunc(HttpServletRequest request){
        Claims token = jwtService.checkAuthorizationHeader(request);
        if(token == null){
            System.out.println("[Error] Token is Missed");
            return null;
        }
        return token;
    }
    private JWTService jwtService = new JWTService();
    @PostMapping("/api/character/init")
    @ResponseBody
    public String initChar(HttpServletRequest request, @RequestBody List<CharacterInfoDto> characterInfoDtoList){
        // 로그인 정보 확인
        Claims token = jwtCheckFunc(request);
        // 유저 확인
        if(token == null) return null;
        Integer id = Integer.parseInt(token.get("id").toString());

        boolean isInit = characterService.init(id, characterInfoDtoList);
        if(!isInit){
            System.out.println("[Error] Init Error");
            return "Init Failed";
        }
        return "Init Success";
    }

    @GetMapping("/api/character/user/get-chars")
    @ResponseBody
    public List<CharacterInfoDto> getUserCharacters(HttpServletRequest request){
        // 유저 정보 확인
        Claims token = jwtCheckFunc(request);
        if(token == null) return null;
        Integer id = Integer.parseInt(token.get("id").toString());

        // 로그인한 유저의 캐릭터들 가져오기
        List<CharacterInfoDto> characters = characterService.getCharacterByUserId(id);
        if(characters == null) System.out.println("[Alert] No Characters");
        System.out.println("[Alert] Get Characters Success");
        return characters;
    }

    @PostMapping("/api/character/user/update-chars")
    @ResponseBody
    public String updateUserCharacters(HttpServletRequest request, @RequestBody List<CharacterInfoDto> characters){
        // 유저 정보 확인
        Claims token = jwtCheckFunc(request);
        if(token == null) return null;
        Integer id = Integer.parseInt(token.get("id").toString());
        // 기존 캐릭터들 가져오기
        boolean isUpdate = characterService.updateCharacters(id, characters);

        if(!isUpdate) return "Update Failed";
        return "Update Succcess";
    }
}
