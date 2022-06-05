package com.pucp.odiparpackappback.Repositories;

import com.pucp.odiparpackappback.models.PedidoModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PedidoRepository extends CrudRepository<PedidoModel, Long> {
    List<PedidoModel> findPedidoModelByFechaHoraCreacionBetween(Date fechaInicio, Date fechaFin);
}
