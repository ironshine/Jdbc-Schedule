package com.sparta.springmasterweek1;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDTO {
    private long id;
    private String toDo;
    private String name;
    private String password;
    private LocalDateTime dateTime;

    public ScheduleResponseDTO(Schedule schedule) {
        this.id = schedule.getId();
        this.toDo = schedule.getToDo();
        this.name = schedule.getName();
        this.password = schedule.getPassword();
        this.dateTime = schedule.getDateTime();
    }
}