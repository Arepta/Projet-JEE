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
    
    private final RoomRepository roomRepository;

    @Autowired // IMPORTANT
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAll() {
        return this.roomRepository.findAll(); //build in
    }

    public Map<Long, String> getAllIdxName() {

        List<Room> b = this.roomRepository.findAll();
        Map<Long, String> r = new HashMap<>();
        for(Room cl : b){
            r.put(cl.getId(), cl.getName());
        }
        return r; //build in
    }

    public Optional<Room> getById(Long id) {
        return this.roomRepository.findById(id);  //build in
    }

    public Optional<Room> getByName(String name) {
        return this.roomRepository.findByName(name);  //build in
    }

    public Room create(Room RoomDetails) {
        return this.roomRepository.save(RoomDetails);  //build in
    }

    public Room update(Room RoomDetails) {
        Optional<Room> optionalRoom = this.roomRepository.findById(RoomDetails.getId());

        if (optionalRoom.isPresent()) {
            Room updatedRoom = optionalRoom.get();

            updatedRoom.setName(RoomDetails.getName());
            updatedRoom.setSize(RoomDetails.getSize());

            return this.roomRepository.save(updatedRoom);
        }

        return null;
    }

    public void delete(Long id) {
        this.roomRepository.deleteById(id);
    }
}
