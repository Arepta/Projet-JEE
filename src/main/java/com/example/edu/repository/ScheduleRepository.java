package com.example.edu.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.Schedule;

public interface ScheduleRepository  extends JpaRepository<Schedule, Long> {
    
    @Query(value = "SELECT COUNT(*) FROM schedule WHERE teacher = %:teacher% AND start >= %:start% AND end <= %:end%", nativeQuery = true)
    Long countTeacherUseBetween(@Param("teacher") Long teacher, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT COUNT(*) FROM schedule WHERE room = %:room% AND start >= %:start% AND end <= %:end%", nativeQuery = true)
    Long countRoomUseBetween(@Param("room") Long room, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
