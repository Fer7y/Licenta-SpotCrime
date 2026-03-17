package com.licenta.crimerate.repository;

import com.licenta.crimerate.entity.IncidentRaportat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IncidentRaportatRepository extends JpaRepository<IncidentRaportat, Integer> {
    List<IncidentRaportat> findByStatus(String status);
    List<IncidentRaportat> findByLocalitateId(Integer localitateId);
    List<IncidentRaportat> findByUtilizatorRaportorId(Integer utilizatorId);

}
