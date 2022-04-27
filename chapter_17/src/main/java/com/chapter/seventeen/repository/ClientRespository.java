package com.chapter.seventeen.repository;

import com.chapter.seventeen.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRespository extends JpaRepository<Client, Integer> {
    Optional<Client> findClientByClientId(String clientId);
}
