package cz.aimtec.enviserver.controller;

import org.springframework.data.repository.CrudRepository;

import cz.aimtec.enviserver.model.Measurement;

public interface MeasurementRepository extends CrudRepository<Measurement, Long> {

	Iterable<Measurement> findBySensorUUID(Long sensorUUID);
}