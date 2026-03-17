package com.licenta.crimerate.repository;

import com.licenta.crimerate.entity.IstoricExport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IstoricExportRepository extends JpaRepository<IstoricExport, Integer> {
    List<IstoricExport> findByUtilizatorId(Integer utilizatorId);
}
