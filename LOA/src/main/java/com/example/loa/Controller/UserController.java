package com.example.loa.Controller;

import com.example.loa.Dto.UserDto;
import com.example.loa.Entity.User;
import com.example.loa.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // User 단일 조회
    @GetMapping("/user/getUser/{id}")
    @ResponseBody
    public UserDto getUser(@PathVariable Integer id){

        Optional<User> userOptional = userService.getUserById(id);  // DB에서 id로 검색

        if(!userOptional.isPresent()) return null;  // 비어있으면 null 리턴

        User user =  userOptional.get();

        UserDto responseUser = UserDto.toDto(user);

        return responseUser;
    }

    // User 로그인
    @PostMapping("auth/login")
    @ResponseBody
    public String login(@RequestBody UserDto userDto){

        String token = userService.userAuth(userDto);

        // 예외처리 및 응답
        if(token.equals("Null Error")){
            return "Id or Password Requried";
        }else if(token.equals("Id Error")){
            return "Id is Incorrected";
        }else if(token.equals("Password Error")){
            return "Check Your Password";
        }else return token;
    }

    // User 회원가입
    @PostMapping("/auth/sign") // 회원가입
    @ResponseBody
    public String signUp(@RequestBody UserDto userDto){

        System.out.println(userService.getUserById(1));
        // user의 id로 중복 조회
        User userTmp = userService.getUserByUserId(userDto.getUserId());

        if(userTmp != null) {   // 계정이 있을 경우
            return "Account Already Existed";
        }


        boolean isCreated = userService.createUser(User.toEntity(userDto));

        if(!isCreated){
            return "Create Failed";
        }
        return "success";
    }
}