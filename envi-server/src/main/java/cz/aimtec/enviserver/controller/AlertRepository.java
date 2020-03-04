package cz.aimtec.enviserver.controller;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import cz.aimtec.enviserver.model.Alert;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends CrudRepository<Alert, Long>, JpaSpecificationExecutor<Alert> {

	public Iterable<Alert> findBySensorUUID(String UUID);
	
}