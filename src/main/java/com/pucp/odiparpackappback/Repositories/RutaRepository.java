package com.pucp.odiparpackappback.Repositories;

import com.pucp.odiparpackappback.models.RutaModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutaRepository extends CrudRepository<RutaModel, Long> {
    List<RutaModel> findByIdUnidadTransporte(long idVehicule);
}
