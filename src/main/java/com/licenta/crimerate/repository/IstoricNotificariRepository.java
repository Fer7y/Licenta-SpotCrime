package com.licenta.crimerate.repository;

import com.licenta.crimerate.entity.IstoricNotificari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IstoricNotificariRepository extends JpaRepository<IstoricNotificari, Integer> {
    List<IstoricNotificari> findByUtilizatorId(Integer utilizatorId);
    List<IstoricNotificari> findByUtilizatorIdAndCitit(Integer utilizatorId, Boolean citit);
    List<IstoricNotificari> findByUtilizatorIdOrderByDataNotificareDesc(Integer utilizatorId);
}
