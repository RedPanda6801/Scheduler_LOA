package com.example.loa.Service;

import com.example.loa.Dto.UserDto;
import com.example.loa.Entity.User;
import com.example.loa.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private JWTService jwtService = new JWTService();

    private BcryptService bcryptService = new BcryptService();

    public Optional<User> getUserById(Integer id){
        Optional<User> user = userRepository.findById(id);

        return user;
    }

    public User getUserByUserId(String userid){
        User user = userRepository.findByUserId(userid);

        if(user == null){
            return null;
        }
        return user;
    }

    public boolean createUser(User user){
        try{
            User userTmp = user;
            String hash = bcryptService.encodeBcrypt(user.getPassword(), 10);
            userTmp.setPassword(hash);
            userRepository.save(user);
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    public String userAuth(UserDto userDto){
        String userId = userDto.getUserId();
        String userPass = userDto.getPassword();
        // 입력된 데이터 확인
        if(userId == null && userPass == null) return "Null Error";

        // 유저 아이디 확인
        User user = getUserByUserId(userId);

        if(user == null) return "Id Error";

        /// 유저 비밀번호 확인
        // 비밀번호 불일치시 예외처리 - hash화된 비밀번호를 bcrypt로 비교
        boolean hashCheck = bcryptService.matchesBcrypt(userPass, user.getPassword(), 10);
        if(!hashCheck){
            return "Password Error";
        }
        String token = jwtService.makeJwtToken(user.getId(), userId);

        // Model에 token을 같이 넘겨주어 프론트에서 localStorage에 저장하게끔 구현

        return token;
    }
}
