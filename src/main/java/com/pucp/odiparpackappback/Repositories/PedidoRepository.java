package com.pucp.odiparpackappback.Repositories;

import com.pucp.odiparpackappback.models.EstadoPedido;
import com.pucp.odiparpackappback.models.PedidoModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PedidoRepository extends CrudRepository<PedidoModel, Long> {
    List<PedidoModel> findPedidoModelByFechaHoraCreacionBetween(Date fechaInicio, Date fechaFin);
    List<PedidoModel> findPedidoModelByEstadoIs(EstadoPedido ep);

    @Modifying
    @Transactional
    @Query("UPDATE PedidoModel p SET p.estado = 3 WHERE p.id = ?1")
    public void actualizarEstadoEntregado(long idPedido);
}