package com.example.edu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.Classes;

public interface ClassesRepository extends JpaRepository< Classes, Long>{

    @Query(value = "SELECT * FROM class WHERE name = %:name% LIMIT 1", nativeQuery = true)
    Optional<Classes> findByName(@Param("name") String name);

    @Query(value = "SELECT class.id, class.title, class.program, class.size" + 
                "FROM class" +
                "LEFT JOIN students ON class.id = students.class " +
                "GROUP BY class.id, class.title, class.program, class.size " +
                "HAVING COUNT(students.id) < class.size;", nativeQuery = true)
    Optional<List<Classes>> findNotFullClasses();

    @Query(value = "SELECT class.id, class.title, class.program, class.size" + 
    "FROM class" +
    "LEFT JOIN students ON class.id = students.class " +
    "WHERE program = %:programId% GROUP BY class.id, class.title, class.program, class.size " +
    "HAVING COUNT(students.id) < class.size;", nativeQuery = true)
    Optional<List<Classes>>  findNotFullClassesForProgram(@Param("programId") Long programId);

}
