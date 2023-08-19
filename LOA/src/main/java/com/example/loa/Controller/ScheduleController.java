package com.example.loa.Controller;

import com.example.loa.Dto.ScheduleDto;
import com.example.loa.Service.JWTService;
import com.example.loa.Service.ScheduleService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;
    JWTService jwtService = new JWTService();
    @PostMapping("/api/schedule/check")
    @ResponseBody
    public String checkSchedule(HttpServletRequest request, @RequestBody ScheduleDto dto){
        // 유저 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return null;

        Boolean isChecked = scheduleService.checkSchedule(dto);
        if(!isChecked) return "Check Failed";
        return "Check Success";
    }

    @PostMapping("/api/schedule/reset")
    @ResponseBody
    public String resetSchedule(HttpServletRequest request){
        // 유저 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return null;

        Integer userId = Integer.parseInt(token.get("id").toString());

        Boolean isReset = scheduleService.resetSchedule(userId);
        if(!isReset) return "Reset Failed";
        return "Reset Success";
    }
}
