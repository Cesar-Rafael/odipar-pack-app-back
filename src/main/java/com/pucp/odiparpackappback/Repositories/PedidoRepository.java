package com.pucp.odiparpackappback.Repositories;

import com.pucp.odiparpackappback.models.PedidoModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends CrudRepository<PedidoModel, Long> {

}
