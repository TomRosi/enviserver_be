package cz.aimtec.enviserver.controller;

import cz.aimtec.enviserver.model.Sensor;
import cz.aimtec.enviserver.model.SensorTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class SensorTableController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SensorTableRepository sensorTableRepository;

	@GetMapping(path = "/sensors")
	public @ResponseBody Iterable<SensorTable> getAllIssues(@RequestHeader(value = "UUID") String UUID) {

		logger.debug("Fetching measurements.");

		if (Sensor.isUUIDValid(UUID)) {

			if (UUID.equals(Sensor.MASTER_UUID)) {
				Stream<SensorTable> stream = StreamSupport.stream(sensorTableRepository.findAll().spliterator(), true);
				return stream.collect(Collectors.toList());

			} else {
				Stream<SensorTable> stream = StreamSupport.stream(sensorTableRepository.findBySensorUUID(UUID).spliterator(), true);
				return stream.collect(Collectors.toList());
			}
		} else {
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
		}
	}

	@PostMapping(path = "/sensors")
	public @ResponseBody ResponseEntity<String> addNewIssue(@RequestBody SensorTable record,
															@RequestHeader(value = "UUID") String UUID) {
		logger.debug("Storing an sensor.");
		if (Sensor.isUUIDValid(UUID)) {
			SensorTable newSensor = new SensorTable((record.getSensorUUID() == null) ? null : record.getSensorUUID(),
					(record.getName() == null) ? null : record.getName(),
					(record.getLocation() == null) ? null : record.getLocation(),
					(record.getMinTemperature()== null) ? null : record.getMinTemperature(),
					(record.getMaxTemperature() == null) ? null : record.getMaxTemperature());
			sensorTableRepository.save(newSensor);
			return new ResponseEntity<>("Sensor stored.", HttpStatus.OK);
		} else {
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
		}
	}

	@GetMapping(path = "/sensors/{id}")
	public @ResponseBody SensorTable getById(@PathVariable Long id, @RequestHeader(value = "UUID") String UUID) {
		logger.debug("Getting a single sensor (id = {}).", id);

		if (Sensor.isUUIDValid(UUID)) {
			if (sensorTableRepository.findOne(id).getSensorUUID().equals(UUID) || UUID.equals(Sensor.MASTER_UUID)) {
				return sensorTableRepository.findOne(id);
			} else {
				throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidMeasurementUUID);
			}
		} else {
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
		}

	}

	@PutMapping("/sensors/{id}")
	public @ResponseBody SensorTable updateById(@PathVariable Long id, @RequestBody SensorTable record, @RequestHeader(value = "UUID") String UUID) {
		logger.debug("Updating an sensor (id = {}).", id);
		if (Sensor.isUUIDValid(UUID)) {
			if (sensorTableRepository.findOne(id).getSensorUUID().equals(UUID) || UUID.equals(Sensor.MASTER_UUID)) {
				SensorTable recordToUpdate = sensorTableRepository.findOne(id);
				if (UUID.equals(Sensor.MASTER_UUID)) {
					recordToUpdate.updateFrom(record, true);
				}
				recordToUpdate.updateFrom(record, false);
				return sensorTableRepository.save(recordToUpdate);
			} else {
				throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidMeasurementUUID);
			}
		} else {
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
		}
	}

	@DeleteMapping(path = "/sensors/{id}")
	public @ResponseBody ResponseEntity<String> deleteById(@PathVariable Long id,
														   @RequestHeader(value = "UUID") String UUID) {
		logger.debug("Deleting an alert (id = {}).", id);
		if (Sensor.isUUIDValid(UUID)) {
			if (sensorTableRepository.findOne(id).getSensorUUID().equals(UUID) || UUID.equals(Sensor.MASTER_UUID)) {
				sensorTableRepository.delete(id);
				return new ResponseEntity<>("Sensor deleted.", HttpStatus.OK);
			} else {
				throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidMeasurementUUID);
			}
		} else {
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
		}
	}
}