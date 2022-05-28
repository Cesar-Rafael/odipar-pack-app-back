package com.pucp.odiparpackappback.Repository;

import com.pucp.odiparpackappback.models.UnidadTransporteModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadTransporteRepository extends CrudRepository<UnidadTransporteModel, Long> {

}