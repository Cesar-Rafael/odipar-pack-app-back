package com.pucp.odiparpackappback.Repositories;

import com.pucp.odiparpackappback.models.ClienteModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends CrudRepository<ClienteModel, Long> {

}
