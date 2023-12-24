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

    Booked findByReservationNumber(String bookedNumber);

    @Query(nativeQuery = true, value = "SELECT * FROM booked WHERE book_id = " +
            "(SELECT MAX(book_id) FROM booked WHERE room_number=:roomNumber and check_in ='check_in')"
    )
    Booked getLastBooking(@Param("roomNumber") String roomNumber);

    @Query("SELECT p FROM Booked p WHERE p.checkOutDate is null")
    List<Booked> fetchAll();

    @Query("SELECT p FROM Booked p WHERE p.checkOutDate is not null and p.checkInDate is not null")
    List<Booked> getRoomsUsage();

    @Query( nativeQuery = true, value =
            "SELECT reservation_number FROM booked  WHERE room_number =:roomNumber " +
                    "and check_in is not null and check_out_date is null")
    String findReservationNumber(@Param("roomNumber") String roomNumber);
}

//if endDate is below any startDate and the