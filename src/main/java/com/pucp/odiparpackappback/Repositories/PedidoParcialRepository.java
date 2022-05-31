package com.pucp.odiparpackappback.Repositories;

import com.pucp.odiparpackappback.models.PedidoParcialModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoParcialRepository extends CrudRepository<PedidoParcialModel, Long> {
}
