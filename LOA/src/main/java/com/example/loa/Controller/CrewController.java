package com.example.loa.Controller;

import com.example.loa.Dto.*;
import com.example.loa.Service.CrewService;
import com.example.loa.Service.JWTService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CrewController {

    @Autowired
    CrewService crewService;

    JWTService jwtService = new JWTService();

    // 크루 존재 여부
    @GetMapping("/api/crew/is-crew/{crewName}")
    @ResponseBody
    public ResponseEntity<ResponseDto> isCrew(HttpServletRequest request, @PathVariable String crewName){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        // Service 호출
        Boolean isExisted = crewService.isCrew(crewName);
        ResponseDto response = new ResponseDto();
        // 크루가 없으면 True
        response.setResponse("isCrew", !isExisted, HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 새 크루 생성
    @PostMapping("/api/crew/create")
    @ResponseBody
    public ResponseEntity<ResponseDto> addCrew(HttpServletRequest request, @RequestBody CrewDto dto){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Integer id = Integer.parseInt(token.get("id").toString());
        // Service 호출
        ResponseDto response = crewService.addCrew(id, dto);
        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 크루 지원서 작성
    @PostMapping("/api/crew/apply-crew")
    @ResponseBody
    public ResponseEntity<ResponseDto> applyCrew(HttpServletRequest request, @RequestBody CrewApplyDto applyDto){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        // Service 호출
        ResponseDto response = crewService.applyCrew(applyDto);
        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 크루 지원서 조회
    @GetMapping("/api/crew/get-applies/{crewName}")
    @ResponseBody
    public ResponseEntity<ResponseDto> getCrewApplies(HttpServletRequest request, @PathVariable String crewName){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Integer id = Integer.parseInt(token.get("id").toString());
        // Service 호출
        ResponseDto response = crewService.getCrewApplies(id, crewName);
        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.FORBIDDEN){
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 크루 지원서 반려
    @PostMapping("/api/crew/delete-applies/{applyId}")
    @ResponseBody
    public ResponseEntity<ResponseDto> deleteCrewApply(HttpServletRequest request, @PathVariable String applyId){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Integer id = Integer.parseInt(token.get("id").toString());
        // Service 호출
        ResponseDto response = crewService.deleteCrewApply(id, applyId);
        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.FORBIDDEN){
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 크루 인원 추가
    @PostMapping("/api/crew/add-member")
    @ResponseBody
    public ResponseEntity<ResponseDto> addMember(HttpServletRequest request, @RequestBody CrewMemberDto dto){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Integer id = Integer.parseInt(token.get("id").toString());
        // Service 호출
        ResponseDto response = crewService.addMember(id, dto);
        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.FORBIDDEN){
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 내 크루 검색
    @GetMapping("/api/crew/get-crew")
    @ResponseBody
    public ResponseEntity<ResponseDto> getMyCrew(HttpServletRequest request){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Integer id = Integer.parseInt(token.get("id").toString());
        // Service 호출
        ResponseDto response = crewService.getCrew(id);
        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.FORBIDDEN){
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 크루 멤버 검색
    @GetMapping("/api/crew/get-members/{crew}")
    @ResponseBody
    public ResponseEntity<ResponseDto> getCrewMembers(HttpServletRequest request, @PathVariable String crew){
        // JWT 확인
        Claims token = jwtService.jwtCheckFunc(request);
        if(token == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        // Service 호출
        ResponseDto response = crewService.getCrewMembers(crew);
        // 응답값 처리
        if(response.getStatus() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else if(response.getStatus() == HttpStatus.FORBIDDEN){
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        else if(response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR){
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
