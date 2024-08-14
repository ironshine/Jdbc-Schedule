package com.sparta.springmasterweek1;

import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ScheduleController {
    private final Map<Long, Schedule> scheduleMap = new HashMap<>();

    @PostMapping("/schedule")
    public ScheduleResponseDTO creatSchedule(ScheduleRequestDTO requestDTO){
        // RequestDto -> Entity
        // 할일, 담당자명, 비밀번호, 작성/수정일을 저장
        Schedule schedule = new Schedule(requestDTO);
        // 기간 정보는 날짜와 시간을 모두 포함한 형태
        // 최초 입력간에는 수정일은 작성일과 동일

        // 각 일정의 고유 식별자(ID)를 자동으로 생성하여 관리
        Long maxId = scheduleMap.size() > 0 ? Collections.max(scheduleMap.keySet()) + 1 : 1;
        schedule.setId(maxId);

        // DB 저장
        scheduleMap.put(schedule.getId(), schedule);

        // Entity -> ResponseDto
        ScheduleResponseDTO scheduleResponseDTO = new ScheduleResponseDTO(schedule);

        return scheduleResponseDTO;
    }

    // 등록된 일정의 정보를 반환 받아 확인
    @GetMapping("/schedule")
    public List<ScheduleResponseDTO> getSchedule() {
        // Map To List
        List<ScheduleResponseDTO> responseList = scheduleMap.values().stream()
                .map(ScheduleResponseDTO::new).toList();

        return responseList;
    }
}