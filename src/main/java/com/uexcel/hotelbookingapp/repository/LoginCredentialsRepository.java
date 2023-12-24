package com.uexcel.hotelbookingapp.repository;

import com.uexcel.hotelbookingapp.entity.LoginCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginCredentialsRepository extends JpaRepository<LoginCredentials,Long>{
    LoginCredentials findByEmail(String email);
}
