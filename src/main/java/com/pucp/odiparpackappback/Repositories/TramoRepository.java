package com.pucp.odiparpackappback.Repositories;

import com.pucp.odiparpackappback.models.TramoModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TramoRepository extends CrudRepository<TramoModel, Long> {

}
