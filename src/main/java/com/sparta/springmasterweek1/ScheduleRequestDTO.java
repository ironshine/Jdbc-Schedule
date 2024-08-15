package com.sparta.springmasterweek1;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleRequestDTO {
    private Long id;
    private String toDo;
    private String name;
    private String password;
    private String dateTime;

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
