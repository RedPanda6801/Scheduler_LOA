package com.example.loa.Controller;

import com.example.loa.Dto.ApiDto;
import com.example.loa.Dto.ResponseDto;
import com.example.loa.Dto.UserDto;
import com.example.loa.Entity.User;
import com.example.loa.Service.JWTService;
import com.example.loa.Service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    private JWTService jwtService = new JWTService();

    // User 단일 조회
    @GetMapping("/api/user/getUser/{id}")
    @ResponseBody
    public UserDto getUser(@PathVariable Integer id){

        Optional<User> userOptional = userService.getById(id);  // DB에서 id로 검색

        if(!userOptional.isPresent()) return null;  // 비어있으면 null 리턴

        User user =  userOptional.get();

        UserDto responseUser = UserDto.toDto(user);

        return responseUser;
    }

    // User 로그인
    @PostMapping("/api/auth/login")
    @ResponseBody
    public ResponseEntity<ResponseDto> login(@RequestBody UserDto userDto){

        ResponseDto response = userService.userAuth(userDto);

        // 예외처리 및 응답
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    // User 회원가입
    @PostMapping("/api/auth/sign") // 회원가입
    @ResponseBody
    public ResponseEntity<ResponseDto> signUp(@RequestBody UserDto userDto){

        // user의 id로 중복 조회
        User userTmp = userService.getByUserId(userDto.getUserId());

        if(userTmp != null) {   // 계정이 있을 경우
            ResponseDto response = new ResponseDto();
            response.setResponse("User already Existed", null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        ResponseDto response = userService.create(User.toEntity(userDto));

        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // User 개인정보 수정
    @PostMapping("/api/user/update/{id}")
    @ResponseBody
    public ResponseEntity<ResponseDto> updateUser(@PathVariable Integer id, @RequestBody UserDto userDto){

        ResponseDto response = userService.update(id, userDto);

        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // User 개인정보 삭제
    @PostMapping("/api/user/delete")
    @ResponseBody
    public ResponseEntity<ResponseDto> deleteUser(HttpServletRequest request){
        // 로그인 정보 확인
        Claims token = jwtService.checkAuthorizationHeader(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        // 유저 확인
        Integer id = Integer.parseInt(token.get("id").toString());

        ResponseDto response = userService.delete(id);

        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // LOA API KEY 추가
    @PostMapping("/api/user/set-key")
    @ResponseBody
    public ResponseEntity<ResponseDto> setUserAPI(HttpServletRequest request, @RequestBody ApiDto dto){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        String id = token.get("id").toString();

        // Service 호출
        ResponseDto response = userService.setKey(dto.getKey(), id);

        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // LOA API KEY 조회
    @GetMapping("/api/user/get-key")
    @ResponseBody
    public ResponseEntity<ResponseDto> getUserAPI(HttpServletRequest request){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        String id = token.get("id").toString();

        // Service 호출
        ResponseDto response = userService.getKey(id);
        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}