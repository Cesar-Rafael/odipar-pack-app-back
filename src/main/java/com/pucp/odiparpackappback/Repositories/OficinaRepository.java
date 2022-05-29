package com.pucp.odiparpackappback.Repositories;

import com.pucp.odiparpackappback.models.OficinaModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OficinaRepository extends CrudRepository<OficinaModel, Long> {

}
