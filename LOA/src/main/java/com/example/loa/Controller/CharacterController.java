package com.example.loa.Controller;

import com.example.loa.Dto.CharacterInfoDto;
import com.example.loa.Dto.ResponseDto;
import com.example.loa.Service.CharacterService;
import com.example.loa.Service.JWTService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // 캐릭터 초기 세팅
    @PostMapping("/api/character/init")
    @ResponseBody
    public ResponseEntity<ResponseDto> initCharacters(HttpServletRequest request, @RequestBody List<CharacterInfoDto> characterInfoDtoList){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Integer id = Integer.parseInt(token.get("id").toString());
        // Service 호출
        ResponseDto response = characterService.init(id, characterInfoDtoList);
        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 계정 단위 캐릭터 조회
    @GetMapping("/api/character/get-chars")
    @ResponseBody
    public ResponseEntity<ResponseDto> getCharacters(HttpServletRequest request){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Integer id = Integer.parseInt(token.get("id").toString());
        // 유저 메인캐릭터 가져오기
        String charName = token.get("mainChar").toString();
        // Service 호출
        List<CharacterInfoDto> dtoList = characterService.getCharacterByUserId(id, charName);
        // 응답값 처리
        ResponseDto response = new ResponseDto();
        if(dtoList == null){
            response.setResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setResponse("Search Success", dtoList, HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 스케줄 관리 캐릭터 번경
    @PostMapping("/api/character/change-chars")
    @ResponseBody
    public ResponseEntity<ResponseDto> changeCharacters(HttpServletRequest request, @RequestBody List<CharacterInfoDto> characters){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Integer id = Integer.parseInt(token.get("id").toString());
        // Service 호출
        ResponseDto response = characterService.changeCharacters(id, characters);
        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 캐릭터 단일 수정
    @PostMapping("api/character/modify-char")
    @ResponseBody
    public ResponseEntity<ResponseDto> modifyCharacter(HttpServletRequest request, @RequestBody CharacterInfoDto character){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        // 캐릭터 단일 수정
        ResponseDto response = characterService.modifyCharacter(character);
        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 계정 단위 캐릭터 정보 최신화
    @PostMapping("api/character/update-chars")
    @ResponseBody
    public ResponseEntity<ResponseDto> updateCharacters(HttpServletRequest request, @RequestBody List<CharacterInfoDto> characters){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        // 캐릭터 최신화
        ResponseDto response = characterService.updateCharacters(characters);
        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
