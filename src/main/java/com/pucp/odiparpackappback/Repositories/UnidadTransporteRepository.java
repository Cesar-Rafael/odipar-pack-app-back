package com.pucp.odiparpackappback.Repositories;

import com.pucp.odiparpackappback.models.EstadoUnidadTransporte;
import com.pucp.odiparpackappback.models.UnidadTransporteModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UnidadTransporteRepository extends CrudRepository<UnidadTransporteModel, Long> {
    ArrayList<UnidadTransporteModel> findUnidadTransporteModelByEstadoEquals(EstadoUnidadTransporte etiqueta);
}