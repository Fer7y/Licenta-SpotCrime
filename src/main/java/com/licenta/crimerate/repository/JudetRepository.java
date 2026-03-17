package com.licenta.crimerate.repository;

import com.licenta.crimerate.entity.Judet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JudetRepository extends JpaRepository<Judet, Integer> {
    Optional<Judet> findByIndicativ(String indicativ);
}
