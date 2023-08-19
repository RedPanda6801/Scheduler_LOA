package com.example.loa.Service;

import com.example.loa.Dto.CharacterInfoDto;
import com.example.loa.Dto.ScheduleDto;
import com.example.loa.Entity.CharacterInfo;
import com.example.loa.Entity.Schedule;
import com.example.loa.Repository.CharacterInfoRepository;
import com.example.loa.Repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        schedule.setValtan(false);
        schedule.setBiakiss(false);
        schedule.setKuke(false);
        schedule.setAbrel12(false);
        schedule.setAbrel34(false);
        schedule.setAbrel56(false);
        schedule.setAkkan(false);
        schedule.setKkayangel(false);
        schedule.setKhamen(false);
        schedule.setSanghatop(false);
        try{
            Schedule result = scheduleRepository.save(schedule);
            return result;
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            return null;
        }
    }
    @Transactional
    public Boolean checkSchedule(ScheduleDto dto){
        Optional<Schedule> scheduleTmp = scheduleRepository.findById(dto.getId());
        if(!scheduleTmp.isPresent()){
            System.out.println("[Error] No Data Error");
            return false;
        }
        try{
            Schedule schedule = scheduleTmp.get();
            schedule.update(dto);
            return true;
        }catch(Exception e){
            System.out.println(String.format("[Error] %s", e));
            return null;
        }
    }
    public Boolean resetSchedule(Integer id){
        // 유저의 캐릭터 찾기
        List<CharacterInfo> characters = characterInfoRepository.findAllByUserId(id);
        if(characters.size() == 0){
            System.out.println("[Error] No Character Existed");
            return false;
        }
        // 캐릭터 별 스캐줄 삭제 -> 생성
        for(CharacterInfo character : characters){
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
        return true;
    }


}
