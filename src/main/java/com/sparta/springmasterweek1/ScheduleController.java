package com.sparta.springmasterweek1;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ScheduleController {
    private final Map<Long, Schedule> scheduleMap = new HashMap<>();

    @PostMapping("/schedule")
    public ScheduleResponseDTO creatSchedule(@RequestBody ScheduleRequestDTO requestDTO) {
        // RequestDto -> Entity
        // 할일, 담당자명, 비밀번호, 작성/수정일을 저장
        // 기간 정보는 날짜와 시간을 모두 포함한 형태
        // 최초 입력간에는 수정일은 작성일과 동일
        requestDTO.setDateTime(LocalDateTime.now());
        Schedule schedule = new Schedule(requestDTO);

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

    // 선택한 일정 단건의 정보를 조회
    @GetMapping("/schedule/{id}")
    public ScheduleResponseDTO findId(@PathVariable Long id) {
        if (scheduleMap.containsKey(id)) {
            // 일정의 고유 식별자(ID)를 사용하여 조회
            Schedule schedule = scheduleMap.get(id);

            ScheduleResponseDTO scheduleResponseDTO = new ScheduleResponseDTO(schedule);

            return scheduleResponseDTO;
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }

    //    @GetMapping("/schedule/find")
//    public List<ScheduleResponseDTO> findDateTimeName(@RequestParam(required = false) String dateTime, @RequestParam(required = false)String name) {
//        if (scheduleMap.containsValue(dateTime) || scheduleMap.containsValue(name)) {
//            if (dateTime != null && name != null) {
//
//            } else if (dateTime != null) {
//
//            } else if (name != null) {
//
//            } else {
//                throw new IllegalArgumentException("Param을 입력해주세요.");
//            }
//        } else {
//            throw new IllegalArgumentException("입력한 값의 일정은 존재하지 않습니다.");
//        }
//    }
    @PostMapping("/schedule/update")
    public ScheduleResponseDTO updateSchedule(@RequestBody ScheduleRequestDTO requestDTO) {
        if (scheduleMap.containsKey(requestDTO.getId())) {
            // 일정의 고유 식별자(ID)를 사용하여 조회
            Schedule schedule = scheduleMap.get(requestDTO.getId());
            if (schedule.getPassword().equals(requestDTO.getPassword())) {
                schedule.setName(requestDTO.getName());
                schedule.setToDo(requestDTO.getToDo());
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            ScheduleResponseDTO scheduleResponseDTO = new ScheduleResponseDTO(schedule);

            return scheduleResponseDTO;
        } else {
            throw new IllegalArgumentException("입력한 id는 존재하지 않습니다.");
        }
    }
}