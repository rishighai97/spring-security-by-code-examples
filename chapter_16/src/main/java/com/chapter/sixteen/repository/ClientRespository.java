package com.chapter.sixteen.repository;

import com.chapter.sixteen.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRespository extends JpaRepository<Client, Integer> {
    Optional<Client> findClientByClientId(String clientId);
}
