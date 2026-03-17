package com.licenta.crimerate.repository;

import com.licenta.crimerate.entity.IstoricCriminalitate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IstoricCriminalitateRepository extends JpaRepository<IstoricCriminalitate, Integer> {
    // Ca să putem filtra datele pe un anumit an pentru Dashboard
    List<IstoricCriminalitate> findByAn(Integer an);

    // Datele pentru un anumit județ
    List<IstoricCriminalitate> findByJudetId(Integer judetId);

    List<IstoricCriminalitate> findByJudetIdOrderByAnAsc(Integer judetId);
}
