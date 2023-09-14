package com.example.loa.Service;

import com.example.loa.Dto.ResponseDto;
import com.example.loa.Dto.ScheduleDto;
import com.example.loa.Entity.CharacterInfo;
import com.example.loa.Entity.Schedule;
import com.example.loa.Repository.CharacterInfoRepository;
import com.example.loa.Repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    CharacterInfoRepository characterInfoRepository;
    public Schedule init(){
        Schedule schedule = new Schedule();
        schedule.setValtan(0);
        schedule.setBiakiss(0);
        schedule.setKuke(0);
        schedule.setAbrel(0);
        schedule.setAkkan(0);
        schedule.setKkayangel(0);
        schedule.setKamen(0);
        schedule.setSanghatop(0);
        try{
            Schedule result = scheduleRepository.save(schedule);
            return result;
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            return null;
        }
    }

    @Transactional
    public ResponseDto checkSchedule(ScheduleDto dto){
        // 응답값 객체 생성
        ResponseDto response = new ResponseDto();
        // 스케줄 조회
        Optional<Schedule> scheduleTmp = scheduleRepository.findById(dto.getId());
        if(!scheduleTmp.isPresent()){
            System.out.println("[Error] No Data Error");
            response.setResponse("Not Existed", HttpStatus.BAD_REQUEST);
            return response;
        }
        Schedule schedule = scheduleTmp.get();
        // 수정
        try{
            schedule.update(dto);
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            response.setResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
        response.setResponse("Check Success", HttpStatus.OK);
        return response;
    }

    public ResponseDto resetSchedule(Integer id) {
        // 응답값 객체 생성
        ResponseDto response = new ResponseDto();

        // 유저의 캐릭터 찾기
        List<CharacterInfo> characters = characterInfoRepository.findAllByUserId(id);
        if (characters.size() == 0) {
            System.out.println("[Error] No Character Existed");
            response.setResponse("Not Existed", HttpStatus.BAD_REQUEST);
            return response;
        }
        try {
            // 캐릭터 별 스캐줄 삭제 -> 생성
            for (CharacterInfo character : characters) {
                // 캐릭터에 연결된 스케줄을 찾아 삭제
                Schedule deleteData = character.getSchedule();
                // 연관관계 삭제
                character.setSchedule(null);
                // 스캐줄 Entity 재생성 및 연관관계 생성
                Schedule initSchedule = init();
                character.setSchedule(initSchedule);
                // 데이터 삭제
                scheduleRepository.delete(deleteData);
            }
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            response.setResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
        System.out.println("[Alert] Reset Success");
        response.setResponse("Reset Success", HttpStatus.OK);
        return response;
    }

    public ResponseDto getUserSchedule(List<Integer> ids){
        // 응답값 객체 생성
        ResponseDto response = new ResponseDto();

        // 유저 정보로 캐릭터 가져오기
        List<ScheduleDto> dtos = new ArrayList<>();
        try{
            for(Integer id : ids){
                Optional<CharacterInfo> character = characterInfoRepository.findById(id);
                Schedule schedule = character.get().getSchedule();
                dtos.add(ScheduleDto.toDto(schedule));
            }
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            response.setResponse("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
        if(dtos.size() == 0){
            System.out.println("[Alert] No Data Existed");
        }
        response.setResponse("Search Success", dtos, HttpStatus.OK);
        return response;
    }
}
