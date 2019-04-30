package cz.aimtec.enviserver.controller;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import cz.aimtec.enviserver.model.Measurement;

public interface MeasurementRepository extends CrudRepository<Measurement, Long>, JpaSpecificationExecutor<Measurement> {

	public Iterable<Measurement> findBySensorUUID(String UUID);
	
}