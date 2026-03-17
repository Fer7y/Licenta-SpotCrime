package com.licenta.crimerate.repository;

import com.licenta.crimerate.entity.AbonamentAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AbonamentAlertaRepository extends JpaRepository<AbonamentAlerta, Integer> {
    List<AbonamentAlerta> findByUtilizatorId(Integer utilizatorId);
    List<AbonamentAlerta> findByLocalitateId(Integer localitateId);

    boolean existsByUtilizatorIdAndLocalitateId(Integer utilizatorId, Integer localitateId);
}
