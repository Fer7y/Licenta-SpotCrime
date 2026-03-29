package com.licenta.crimerate.repository;

import com.licenta.crimerate.entity.AplicatieAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AplicatieAdminRepository extends JpaRepository<AplicatieAdmin, Integer> {
    Optional<AplicatieAdmin> findByUtilizatorIdAndStatus(Integer utilizatorId, String status);
    List<AplicatieAdmin> findByUtilizatorIdOrderByDataAplicareDesc(Integer utilizatorId);
    List<AplicatieAdmin> findByStatusOrderByDataAplicareAsc(String status);
}