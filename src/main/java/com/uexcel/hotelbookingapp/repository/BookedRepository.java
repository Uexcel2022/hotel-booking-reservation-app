package com.uexcel.hotelbookingapp.repository;

import com.uexcel.hotelbookingapp.entity.Booked;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookedRepository extends JpaRepository<Booked,Long> {
}
