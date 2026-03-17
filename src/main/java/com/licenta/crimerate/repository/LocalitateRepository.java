package com.licenta.crimerate.repository;

import com.licenta.crimerate.entity.Localitate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocalitateRepository extends JpaRepository<Localitate, Integer> {
    List<Localitate> findByJudetId(Integer judetId);
    List<Localitate> findTop10ByNumeLocalitateContainingIgnoreCase(String nume);
}
