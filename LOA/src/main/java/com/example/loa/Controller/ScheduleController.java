package com.example.loa.Controller;

import com.example.loa.Dto.ResponseDto;
import com.example.loa.Dto.ScheduleDto;
import com.example.loa.Service.JWTService;
import com.example.loa.Service.ScheduleService;
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
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;
    JWTService jwtService = new JWTService();

    // 스케줄 체크
    @PostMapping("/api/schedule/check")
    @ResponseBody
    public ResponseEntity<ResponseDto> checkSchedule(HttpServletRequest request, @RequestBody ScheduleDto dto){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        // Service 호출
        ResponseDto response = scheduleService.checkSchedule(dto);
        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 스케줄 초기화
    @PostMapping("/api/schedule/reset")
    @ResponseBody
    public ResponseEntity<ResponseDto> resetSchedule(HttpServletRequest request){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Integer userId = Integer.parseInt(token.get("id").toString());
        // Service 호출
        ResponseDto response = scheduleService.resetSchedule(userId);
        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 계정 단위 스케줄 조회
    @GetMapping("/api/schedule/get/user-schedules")
    @ResponseBody
    public ResponseEntity<ResponseDto> getUserSchedule(HttpServletRequest request, @RequestBody List<Integer> ids){
        // 유저 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        // Service 호출
        ResponseDto response = scheduleService.getUserSchedule(ids);
        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
