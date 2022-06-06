package com.pucp.odiparpackappback.Repositories;

import com.pucp.odiparpackappback.models.BloqueoModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BloqueoRepository extends CrudRepository<BloqueoModel, Long> {
    @Query("select b from BloqueoModel b where ((b.ubigeoInicio = ?1 and b.ubigeoFin = ?2) or (b.ubigeoInicio = ?2 and b.ubigeoFin = ?1)) and b.fechaInicio <= ?3 and ?4 <= b.fechaFin")
    List<BloqueoModel> findBloqueoModelByUbigeoInicioAndUbigeoFin(int ubigeoInicio, int ubigeoFin, Date fechaInicio, Date fechaFin);
}