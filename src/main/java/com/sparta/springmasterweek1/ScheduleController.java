package com.sparta.springmasterweek1;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final JdbcTemplate jdbcTemplate;

    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final Map<Long, Schedule> scheduleMap = new HashMap<>();

    @PostMapping("/schedule")
    public ScheduleResponseDTO creatSchedule(@RequestBody ScheduleRequestDTO requestDTO) {
        // RequestDto -> Entity
        // 할일, 담당자명, 비밀번호, 작성/수정일을 저장
        // 기간 정보는 날짜와 시간을 모두 포함한 형태
        // 최초 입력간에는 수정일은 작성일과 동일
        requestDTO.setDateTime(String.valueOf(LocalDateTime.now()));
        Schedule schedule = new Schedule(requestDTO);

        // 각 일정의 고유 식별자(ID)를 자동으로 생성하여 관리
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        // DB 저장
        String sql = "INSERT INTO schedule (toDo, name, password, dateTime) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, schedule.getToDo());
                    preparedStatement.setString(2, schedule.getName());
                    preparedStatement.setString(3, schedule.getPassword());
                    preparedStatement.setString(4, schedule.getDateTime());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        schedule.setId(id);

        // Entity -> ResponseDto
        ScheduleResponseDTO scheduleResponseDTO = new ScheduleResponseDTO(schedule);

        return scheduleResponseDTO;
    }

    // 등록된 일정의 정보를 반환 받아 확인
    @GetMapping("/schedule")
    public List<ScheduleResponseDTO> getSchedule() {
        // DB 조회
        String sql = "SELECT * FROM schedule";

        return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDTO>() {
            @Override
            public ScheduleResponseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 schedule 데이터들을 ScheduleResponseDTO 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String toDo = rs.getString("toDo");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String dateTime = rs.getString("dateTime");
                return new ScheduleResponseDTO(id, toDo, name, password, dateTime);
            }
        });
    }

    // 선택한 일정 단건의 정보를 조회
    @GetMapping("/schedule/{id}")
    public ScheduleResponseDTO findId(@PathVariable Long id) {
        // 해당 schedule이 DB에 존재하는지 확인
        Schedule schedule = findById(id);
        if(schedule != null) {
            // schedule id로 조회
            String sql = "SELECT * FROM schedule WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, new RowMapper<ScheduleResponseDTO>() {
                @Override
                public ScheduleResponseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                    // SQL 의 결과로 받아온 schedule 데이터들을 ScheduleResponseDTO 타입으로 변환해줄 메서드
                    Long id = rs.getLong("id");
                    String toDo = rs.getString("toDo");
                    String name = rs.getString("name");
                    String password = rs.getString("password");
                    String dateTime = rs.getString("dateTime");
                    return new ScheduleResponseDTO(id, toDo, name, password, dateTime);
                }
            }, id);
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }

    @GetMapping("/schedule/find/")
    public List<ScheduleResponseDTO> findDateTimeName(@RequestParam(required = false) String dateTime, @RequestParam(required = false)String name) {
        String sql ="";
        if (dateTime != null && name != null) {
            sql = "SELECT * FROM schedule WHERE dateTime LIKE ('?%') AND name = '?' ORDER BY dateTime DESC";
        } else if (dateTime != null && name == null) {
            sql = "SELECT * FROM schedule WHERE dateTime LIKE ('?%') ORDER BY dateTime DESC";
        } else if (dateTime == null && name != null) {
            sql = "SELECT * FROM schedule WHERE name = '?' ORDER BY dateTime DESC";
        } else {
            throw new IllegalArgumentException("Param을 작성해주세요.");
        }
        return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDTO>() {
            @Override
            public ScheduleResponseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 schedule 데이터들을 ScheduleResponseDTO 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String toDo = rs.getString("toDo");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String dateTime = rs.getString("dateTime");
                return new ScheduleResponseDTO(id, toDo, name, password, dateTime);
            }
        });
    }
    @PutMapping("/schedule/update")
    public ScheduleResponseDTO updateSchedule(@RequestBody ScheduleRequestDTO requestDTO) {
        // 해당 메모가 DB에 존재하는지 확인
        Schedule schedule = findById(requestDTO.getId());
        if(schedule != null) {
            if (schedule.getPassword().equals(requestDTO.getPassword())) {
                // schedule 내용 수정
                String sql = "UPDATE schedule SET name = ?, toDo = ?, dateTime = ? WHERE id = ?";
                jdbcTemplate.update(sql, requestDTO.getName(), requestDTO.getToDo(), String.valueOf(LocalDateTime.now()), requestDTO.getId());

                return jdbcTemplate.queryForObject(sql, new RowMapper<ScheduleResponseDTO>() {
                    @Override
                    public ScheduleResponseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                        // SQL 의 결과로 받아온 schedule 데이터들을 ScheduleResponseDTO 타입으로 변환해줄 메서드
                        Long id = rs.getLong("id");
                        String toDo = rs.getString("toDo");
                        String name = rs.getString("name");
                        String password = rs.getString("password");
                        String dateTime = rs.getString("dateTime");
                        return new ScheduleResponseDTO(id, toDo, name, password, dateTime);
                    }
                }, requestDTO.getId());
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/schedule/delete")
    public String deleteSchedule(@RequestBody ScheduleRequestDTO requestDTO) {
        // 해당 schedule이 DB에 존재하는지 확인
        Schedule schedule = findById(requestDTO.getId());
        if(schedule != null) {
            if (schedule.getPassword().equals(requestDTO.getPassword())) {
                // memo 삭제
                String sql = "DELETE FROM schedule WHERE id = ?";
                jdbcTemplate.update(sql, requestDTO.getId());

                return "일정 삭제 완료";
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
            }
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }

    private Schedule findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM schedule WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setToDo(resultSet.getString("toDo"));
                schedule.setName(resultSet.getString("name"));
                schedule.setPassword(resultSet.getString("password"));
                schedule.setDateTime(resultSet.getString("dateTime"));
                return schedule;
            } else {
                return null;
            }
        }, id);
    }
}