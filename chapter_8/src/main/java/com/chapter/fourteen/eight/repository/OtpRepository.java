package com.chapter.fourteen.eight.repository;

import com.chapter.fourteen.eight.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Integer> {
    Optional<Otp> findOtpByUsername(String username);
}
