package com.licenta.crimerate.repository;

import com.licenta.crimerate.entity.PredictieMl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PredictieMlRepository extends JpaRepository<PredictieMl, Integer> {
    List<PredictieMl> findByJudetId(Integer judetId);
    List<PredictieMl> findByAnVizat(Integer anVizat);
}
