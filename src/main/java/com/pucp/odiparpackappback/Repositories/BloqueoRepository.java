package com.pucp.odiparpackappback.Repositories;

import com.pucp.odiparpackappback.models.BloqueoModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloqueoRepository extends CrudRepository<BloqueoModel, Long> {

}