package com.example.edu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long>{

    @Query(value = "SELECT * FROM rooms WHERE name = %:name% LIMIT 1", nativeQuery = true)
    Optional<Room> findByName(@Param("name") String name);

}
