package com.example.loa.Service;

import com.example.loa.Dto.UserDto;
import com.example.loa.Entity.User;
import com.example.loa.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        User user = userRepository.findByUserId(userid);

        if(user == null){
            System.out.println("[Alert] No User");
            return null;
        }
        return user;
    }

    public boolean create(User user){
        try{
            User userTmp = user;
            String hash = bcryptService.encodeBcrypt(user.getPassword(), 10);
            userTmp.setPassword(hash);
            userRepository.save(user);
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            return false;
        }
        return true;
    }

    public String userAuth(UserDto userDto){
        String userId = userDto.getUserId();
        String userPass = userDto.getPassword();
        // 입력된 데이터 확인
        if(userId == null && userPass == null){
            System.out.println("[Error] Null Error");
            return "Null Error";
        }

        // 유저 아이디 확인
        User user = getByUserId(userId);

        if(user == null){
            System.out.println("[Error] Id Error");
            return "Id Error";
        }

        /// 유저 비밀번호 확인
        // 비밀번호 불일치시 예외처리 - hash화된 비밀번호를 bcrypt로 비교
        boolean hashCheck = bcryptService.matchesBcrypt(userPass, user.getPassword(), 10);
        if(!hashCheck){
            System.out.println("[Error] Password Error");
            return "Password Error";
        }
        String token = jwtService.makeJwtToken(user.getId(), userId);

        // Model에 token을 같이 넘겨주어 프론트에서 localStorage에 저장하게끔 구현

        return token;
    }
    @Transactional
    public boolean update(Integer id, UserDto userDto){
        Optional<User> userTmp = getById(id);
        // 수정할 유저가 존재하지 않으면 false 리턴
        if(!userTmp.isPresent()){
            System.out.println("[Error] User Does Not Existed");
            return false;
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
            return false;
        }

        return true;
    }

    public boolean delete(Integer id){
        // 유저 찾기
        User user = getById(id).get();

        // 유저 삭제
        try{
            userRepository.delete(user);
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            return false;
        }
        return true;
    }


}
