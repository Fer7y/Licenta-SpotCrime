package com.licenta.crimerate.repository;

import com.licenta.crimerate.entity.Utilizator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilizatorRepository extends JpaRepository<Utilizator, Integer> {
    // Foarte importantă pentru pagina de Login!
    Optional<Utilizator> findByEmail(String email);
}
