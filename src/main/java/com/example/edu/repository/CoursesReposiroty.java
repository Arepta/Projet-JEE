package com.example.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.Courses;

public interface CoursesReposiroty extends JpaRepository<Courses, Long> {
    @Query(value = "SELECT DISTINCT teachers.id FROM courses JOIN teachers ON courses.field = teachers.field WHERE courses.id = %:id%", nativeQuery = true)
    List<Long> getTeacherForId(@Param("id") Long id);
}
