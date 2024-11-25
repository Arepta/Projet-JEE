package com.example.edu.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.Schedule;

public interface ScheduleRepository  extends JpaRepository<Schedule, Long> {

    @Query(value = "SELECT * FROM schedule WHERE teacher = %:teacher%", nativeQuery = true)
    List<Schedule> findByTeacherId(@Param("teacher") Long teacher);

    @Query(value = "SELECT * FROM schedule WHERE class = %:classes%", nativeQuery = true)
    List<Schedule> findByClassId(@Param("classes") Long classes);
    
    @Query(value = "SELECT COUNT(*) FROM schedule WHERE teacher = %:teacher% AND ( start <= %:end% AND end >= %:start%)", nativeQuery = true)
    Long countTeacherUseBetween(@Param("teacher") Long teacher, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT COUNT(*) FROM schedule WHERE room = %:room% AND ( start <= %:end% AND end >= %:start%)", nativeQuery = true)
    Long countRoomUseBetween(@Param("room") Long room, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT COUNT(*) FROM schedule WHERE class = %:classes% AND ( start <= %:end% AND end >= %:start%)", nativeQuery = true)
    Long countClassBetween(@Param("classes") Long classes, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT COUNT(*) FROM schedule WHERE teacher = %:teacher% AND ( start <= %:end% AND end >= %:start%) AND id != %:id%", nativeQuery = true)
    Long countTeacherUseBetweenId(@Param("teacher") Long teacher, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("id") Long id);

    @Query(value = "SELECT COUNT(*) FROM schedule WHERE room = %:room% AND ( start <= %:end% AND end >= %:start%) AND id != %:id%", nativeQuery = true)
    Long countRoomUseBetweenId(@Param("room") Long room, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("id") Long id);

    @Query(value = "SELECT COUNT(*) FROM schedule WHERE class = %:classes% AND ( start <= %:end% AND end >= %:start%) AND id != %:id%", nativeQuery = true)
    Long countClassBetweenId(@Param("classes") Long classes, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("id") Long id);
}
