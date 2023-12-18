package com.uexcel.hotelbookingapp.repository;

import com.uexcel.hotelbookingapp.entity.Booked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookedRepository extends JpaRepository<Booked,Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM booked WHERE booked_end_date >= :bookStartDate")
    List<Booked> checkRoomAvailability(@Param("bookStartDate") LocalDate bookStartDate);


    @Query(nativeQuery = true, value = "SELECT * FROM booked WHERE room_number =:roomNumber")
    List<Booked> findByRoomNumber(@Param("roomNumber") String roomNumber);
}

//if endDate is below any startDate and the