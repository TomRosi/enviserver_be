package cz.aimtec.enviserver.controller;

import cz.aimtec.enviserver.model.SensorTable;
import org.springframework.data.repository.CrudRepository;

public interface SensorTableRepository extends CrudRepository<SensorTable, Long> {

	public Iterable<SensorTable> findBySensorUUID(String UUID);

}