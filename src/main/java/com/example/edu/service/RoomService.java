package com.example.edu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edu.model.Room;
import com.example.edu.repository.RoomRepository;

@Service
public class RoomService {

    // Repository dependency for managing room data
    private final RoomRepository roomRepository;

    // Constructor injection for RoomRepository
    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // Method to retrieve all rooms
    public List<Room> getAll() {
        return this.roomRepository.findAll();
    }

    // Method to retrieve a mapping of room IDs with their names
    public Map<Long, String> getAllIdxName() {
        List<Room> roomList = this.roomRepository.findAll();
        Map<Long, String> result = new HashMap<>();
        for (Room room : roomList) {
            result.put(room.getId(), room.getName());
        }
        return result;
    }

    // Method to retrieve a room by its ID
    public Optional<Room> getById(Long id) {
        return this.roomRepository.findById(id);
    }

    // Method to retrieve a room by its name
    public Optional<Room> getByName(String name) {
        return this.roomRepository.findByName(name);
    }

    // Method to create a new room
    public Room create(Room roomDetails) {
        return this.roomRepository.save(roomDetails);
    }

    // Method to update an existing room
    public Room update(Room roomDetails) {
        Optional<Room> optionalRoom = this.roomRepository.findById(roomDetails.getId());

        if (optionalRoom.isPresent()) {
            Room updatedRoom = optionalRoom.get();
            updatedRoom.setName(roomDetails.getName());
            updatedRoom.setSize(roomDetails.getSize());
            return this.roomRepository.save(updatedRoom);
        }

        return null;
    }

    // Method to delete a room by its ID
    public void delete(Long id) {
        this.roomRepository.deleteById(id);
    }
}
