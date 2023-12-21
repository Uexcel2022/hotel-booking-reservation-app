package com.uexcel.hotelbookingapp.repository;

import com.uexcel.hotelbookingapp.entity.BookingTracker;

import com.uexcel.hotelbookingapp.entity.BookingTrackerIdClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingTrackerRepository extends JpaRepository<BookingTracker, BookingTrackerIdClass> {

//    Optional<BookingTracker> findById(BookingTrackerIdClass bookingTracker);

    List<BookingTracker> findByRoomNumber(String string);


}
