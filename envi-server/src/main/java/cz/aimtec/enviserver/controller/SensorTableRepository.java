package cz.aimtec.enviserver.controller;

import cz.aimtec.enviserver.model.SensorTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorTableRepository extends CrudRepository<SensorTable, Long> {

	public Iterable<SensorTable> findBySensorUUID(String UUID);

	public SensorTable findSensorByid(Long id);

}