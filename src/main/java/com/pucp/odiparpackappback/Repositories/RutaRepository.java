package com.pucp.odiparpackappback.Repositories;

import com.pucp.odiparpackappback.models.RutaModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutaRepository extends CrudRepository<RutaModel, Long> {
    RutaModel findByIdUnidadTransporte(long idVehicule);
}
