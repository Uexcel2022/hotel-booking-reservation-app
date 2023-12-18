package com.uexcel.hotelbookingapp.repository;

import com.uexcel.hotelbookingapp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,String> {

    @Query(value ="SELECT * FROM Room",
             nativeQuery = true )
    List<Room> fetchAllRoom();

    Room findByRoomNumber(String string);


}
