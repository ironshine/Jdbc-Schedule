package com.sparta.springmasterweek1;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDTO {
    private long id;
    private String toDo;
    private String name;
    private String password;
    private String dateTime;

    public ScheduleResponseDTO(Schedule schedule) {
        this.id = schedule.getId();
        this.toDo = schedule.getToDo();
        this.name = schedule.getName();
        this.password = schedule.getPassword();
        this.dateTime = schedule.getDateTime();
    }

    public ScheduleResponseDTO(Long id, String toDo, String name, String password, String dateTime) {
        this.id = id;
        this.toDo = toDo;
        this.name = name;
        this.password = password;
        this.dateTime = dateTime;
    }
}