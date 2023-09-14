package com.example.loa.Service;

import com.example.loa.Dto.ResponseDto;
import com.example.loa.Dto.UserDto;
import com.example.loa.Entity.User;
import com.example.loa.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private JWTService jwtService = new JWTService();

    private BcryptService bcryptService = new BcryptService();

    public Optional<User> getById(Integer id){
        Optional<User> user = userRepository.findById(id);

        return user;
    }

    public User getByUserId(String userid){
        ResponseDto response = new ResponseDto();

        User user = userRepository.findByUserId(userid);

        if(user == null){
            System.out.println("[Alert] No User");
            return null;
        }
        return user;
    }

    public ResponseDto create(User user){
        // 응답값 객체 생성
        ResponseDto response = new ResponseDto();

        if(user.getPassword() == null || user.getCharName() == null || user.getServer() == null || user.getUserId() == null){
            System.out.println("[Error] More Data Need");
            response.setResponse("More Data Need", HttpStatus.BAD_REQUEST);
            return response;
        }

        try{
            User userTmp = user;
            String hash = bcryptService.encodeBcrypt(user.getPassword(), 10);
            userTmp.setPassword(hash);
            userRepository.save(user);
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            response.setResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
        response.setResponse("Create Success", HttpStatus.CREATED);
        return response;
    }

    public ResponseDto userAuth(UserDto userDto){
        // 응닶값 객체 생성
        ResponseDto response = new ResponseDto();

        String userId = userDto.getUserId();
        String userPass = userDto.getPassword();

        // 입력된 데이터 확인
        if(userId == null && userPass == null){
            System.out.println("[Error] Null Error");
            response.setResponse("Null Error", HttpStatus.BAD_REQUEST);
            return response;
        }

        // 유저 아이디 확인
        User user = getByUserId(userId);

        if(user == null){
            System.out.println("[Error] Id Error");
            response.setResponse("Id Error", HttpStatus.BAD_REQUEST);
            return response;
        }

        /// 유저 비밀번호 확인
        // 비밀번호 불일치시 예외처리 - hash화된 비밀번호를 bcrypt로 비교
        boolean hashCheck = bcryptService.matchesBcrypt(userPass, user.getPassword(), 10);
        if(!hashCheck){
            System.out.println("[Error] Password Error");
            response.setResponse("Password Error", HttpStatus.BAD_REQUEST);
            return response;
        }
        String token = jwtService.makeJwtToken(user.getId(), user.getCharName());

        // Model에 token을 같이 넘겨주어 프론트에서 localStorage에 저장하게끔 구현
        response.setResponse("Login Success", token, HttpStatus.OK);
        return response;
    }
    @Transactional
    public ResponseDto update(Integer id, UserDto userDto){
        // 응답값 객체 생성
        ResponseDto response = new ResponseDto();

        Optional<User> userTmp = getById(id);
        // 수정할 유저가 존재하지 않으면 false 리턴
        if(!userTmp.isPresent()){
            System.out.println("[Error] User Does Not Existed");
            response.setResponse("Not Existed", HttpStatus.BAD_REQUEST);
            return response;
        }

        User user = userTmp.get();
        // null 값을 기존 값으로 대체
        String pass = (userDto.getPassword() == null) ? user.getPassword() : userDto.getPassword();
        String name = (userDto.getCharName() == null) ? user.getCharName() : userDto.getCharName();
        String server = (userDto.getServer() == null) ? user.getServer() : userDto.getServer();

        // 비밀번호 해시화
        String hash = bcryptService.encodeBcrypt(pass, 10);

        try{
            user.update(hash, name, server);
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            response.setResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
        response.setResponse("Update Success", HttpStatus.OK);
        return response;
    }

    public ResponseDto delete(Integer id){
        // 응답값 객체 생성
        ResponseDto response = new ResponseDto();

        // 유저 찾기
        Optional<User> userTmp = getById(id);
        if(!userTmp.isPresent()){
            System.out.println("[Error] User Does Not Existed");
            response.setResponse("Not Existed", HttpStatus.BAD_REQUEST);
            return response;
        }

        User user = userTmp.get();

        // 유저 삭제
        try{
            userRepository.delete(user);
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            response.setResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
        response.setResponse("Delete Success", HttpStatus.OK);
        return response;
    }

    @Transactional
    public ResponseDto setKey(String key, String id){
        // 응답값 객체 생성
        ResponseDto response = new ResponseDto();

        try{
            // 유저 가져오기
            Optional<User> userTmp = getById(Integer.parseInt(id));
            if(!userTmp.isPresent()){
                System.out.println("[Error] User doesn't Existed");
                response.setResponse("Not Existed", HttpStatus.BAD_REQUEST);
                return response;
            }
            User user = userTmp.get();
            // Key 값 부여
            user.setKey(key);
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            response.setResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setResponse("Update Success", HttpStatus.OK);
        return response;
    }

    public ResponseDto getKey(String id){
        // 응답값 객체 생성
        ResponseDto response = new ResponseDto();

        try{
            // 유저 정보 가져오기
            Optional<User> userTmp = getById(Integer.parseInt(id));
            if(!userTmp.isPresent()) return null;
            User user = userTmp.get();

            // data 넘기기
            response.setResponse("Search Success", user.getApi(), HttpStatus.OK);
            return response;
        }catch(Exception e) {
            System.out.println(String.format("[Error] %s", e));
            response.setResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
    }
}
