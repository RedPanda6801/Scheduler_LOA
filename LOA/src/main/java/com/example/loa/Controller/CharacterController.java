package com.example.loa.Controller;

import com.example.loa.Dto.CharacterInfoDto;
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

    private JWTService jwtService = new JWTService();

    @PostMapping("/api/character/init")
    @ResponseBody
    public String initCharacters(HttpServletRequest request, @RequestBody List<CharacterInfoDto> characterInfoDtoList){
        // 로그인 정보 확인
        Claims token = jwtService.jwtCheckFunc(request);
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

    @GetMapping("/api/character/get-chars")
    @ResponseBody
    public List<CharacterInfoDto> getCharacters(HttpServletRequest request){
        // 유저 정보 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return null;
        Integer id = Integer.parseInt(token.get("id").toString());

        // 로그인한 유저의 캐릭터들 가져오기
        List<CharacterInfoDto> characters = characterService.getCharacterByUserId(id);
        if(characters == null) System.out.println("[Alert] No Characters");
        System.out.println("[Alert] Get Characters Success");
        return characters;
    }

    @PostMapping("/api/character/change-chars")
    @ResponseBody
    public String changeCharacters(HttpServletRequest request, @RequestBody List<CharacterInfoDto> characters){
        // 유저 정보 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return null;
        Integer id = Integer.parseInt(token.get("id").toString());

        boolean isUpdate = characterService.changeCharacters(id, characters);
        if(!isUpdate) return "Update Failed";
        return "Update Succcess";
    }

    // 캐릭터 단일 수정
    @PostMapping("api/character/modify-char")
    @ResponseBody
    public String modifyCharacter(HttpServletRequest request, @RequestBody CharacterInfoDto character){
        // 유저 정보 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return null;

        // 캐릭터 수정
        Boolean isUpdated = characterService.modifyCharacter(character);
        if(!isUpdated) return "Update Failed";
        return "Update Success";
    }
    // 캐릭터 정보 최신화
    @PostMapping("api/character/update-chars")
    @ResponseBody
    public String updateCharacters(HttpServletRequest request, @RequestBody List<CharacterInfoDto> characters){
        // 유저 정보 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return null;
        // 캐릭터 최신화
        Boolean isUpdated = characterService.updateCharacters(characters);
        if(!isUpdated) return "Update Failed";
        return "Update Success";
    }
}
