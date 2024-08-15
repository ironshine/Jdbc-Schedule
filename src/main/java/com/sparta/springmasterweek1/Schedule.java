package com.sparta.springmasterweek1;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Schedule {
    private long id;
    private String toDo;
    private String name;
    private String password;
    private LocalDateTime dateTime;

    public Schedule(ScheduleRequestDTO requestDTO) {
        this.toDo = requestDTO.getToDo();
        this.name = requestDTO.getName();
        this.password = requestDTO.getPassword();
        this.dateTime = requestDTO.getDateTime();
    }
}
