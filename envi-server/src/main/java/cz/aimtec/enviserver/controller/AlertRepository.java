package cz.aimtec.enviserver.controller;

import org.springframework.data.repository.CrudRepository;

import cz.aimtec.enviserver.model.Alert;

public interface AlertRepository extends CrudRepository<Alert, Long> {

	public Iterable<Alert> findBySensorUUID(String UUID);
	
}