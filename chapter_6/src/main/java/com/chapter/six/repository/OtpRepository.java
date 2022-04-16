package com.chapter.six.repository;

import com.chapter.six.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Integer> {
    Optional<Otp> findOtpByUsername(String username);
}
