package cz.aimtec.enviserver.controller;


import cz.aimtec.enviserver.model.Sensor;
import cz.aimtec.enviserver.model.SensorTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class SensorTableController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SensorTableRepository sensorTableRepository;

	@Autowired
	private UserSensorRepository userSensorRepository;

	@GetMapping(path = "/sensors")
	public @ResponseBody Iterable<SensorTable> getAllIssues(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String location,
			@RequestParam(required = false) String sensorUUID,
			@RequestParam(required = false) String minTemperature,
			@RequestParam(required = false) String maxTemperature,
			@RequestParam(required = false) String selectedUUID,
			@RequestHeader(value = "UUID") String UUID) {

		logger.debug("Fetching sensors.");

		if (Sensor.isUUIDValid(UUID)) {

			SensorTableSpecification sensorName = null;
			SensorTableSpecification sensorLoc = null;
			SensorTableSpecification sensorU = null;
			SensorTableSpecification minTmp = null;
			SensorTableSpecification maxTmp = null;

			//filter by name
			if (isSet(name)) {
				sensorName = new SensorTableSpecification(new SearchCriteria("name", "=", name));
			}
			//filter by location
			if (isSet(location)) {
				sensorLoc = new SensorTableSpecification(new SearchCriteria("location", "=", location));
			}

			// filter by sensor UUID
			if (isSet(sensorUUID)) {
				sensorU = new SensorTableSpecification(new SearchCriteria("uuid", ":", sensorUUID.toString()));
			}

			// filter by temperature
//			if (isSet(highTemperature)) {
//				highTmp = new SensorTableSpecification(new SearchCriteria("temperature", "<", highTemperature));
//			}
//			if (isSet(lowTemperature)) {
//				lowTmp = new SensorTableSpecification(new SearchCriteria("temperature", ">", lowTemperature));
//			}

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

	private boolean isSet(String param) {
		return (param != null && !param.isEmpty());
	}

	@PostMapping(path = "/sensors")
	public @ResponseBody ResponseEntity<String> addNewIssue(@RequestBody SensorTable record, @RequestHeader(value = "UUID") String UUID) {
		logger.debug("Storing a sensor.");
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

	@GetMapping(path = "/usersensors/{id}")
	public @ResponseBody SensorTable[] getUserSensorsById(@PathVariable Integer id) {
		System.out.println(id);
		System.out.println("------------------------------------------------------------------------------------------------------");
		logger.debug("Getting sensors by user id");
		List<SensorTable> sensors = new ArrayList<>();
		List<String> sensorIDs = userSensorRepository.findSensorsIDsByID(id.toString());

		for(String ids : sensorIDs) {
			sensors.add(sensorTableRepository.findSensorByid(Long.parseLong(ids)));
		}

		return sensors.toArray(new SensorTable[sensors.size()]);
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
	public @ResponseBody ResponseEntity<String> deleteById(@PathVariable Long id, @RequestHeader(value = "UUID") String UUID) {
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