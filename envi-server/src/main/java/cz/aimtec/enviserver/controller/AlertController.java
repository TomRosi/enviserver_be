package cz.aimtec.enviserver.controller;

import java.sql.Timestamp;
import java.util.*;

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

import org.springframework.data.jpa.domain.Specifications;

import cz.aimtec.enviserver.model.Alert;
import cz.aimtec.enviserver.model.Sensor;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AlertController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AlertRepository alertRepository;

	@Autowired
	private SensorTableRepository sensorTableRepository;

	// TODO: přejmenovat low a high na min a max, tak jak je to u Measurements
	@GetMapping(path = "/alerts")
	public @ResponseBody Iterable<Alert> getAllIssues(@RequestParam(required = false) String afterTimestamp,
			@RequestParam(required = false) String beforeTimestamp,
			@RequestParam(required = false) String sensorUUID,
			@RequestParam(required = false) String minTemperature,
			@RequestParam(required = false) String maxTemperature,
			@RequestParam(required = false) String selectedUUID,
			@RequestHeader(value = "UUID") String UUID) {

		logger.debug("Fetching measurements.");

		Iterable<Alert> alerts;
		Iterable<SensorTable> sensorsTable;

		if (Sensor.isUUIDValid(UUID)) {

			AlertSpecification sensorU = null;
			AlertSpecification afterTms = null;
			AlertSpecification beforeTms = null;
			AlertSpecification highTmp = null;
			AlertSpecification lowTmp = null;

			if (isSet(afterTimestamp)) {
				Timestamp measurementAfterTimestamp = Timestamp.valueOf(afterTimestamp);
				afterTms = new AlertSpecification(
						new SearchCriteria("createdOn", ">", measurementAfterTimestamp.toString()));
			}
			if (isSet(beforeTimestamp)) {
				Timestamp measurementBeforeTimestamp = Timestamp.valueOf(beforeTimestamp);
				beforeTms = new AlertSpecification(
						new SearchCriteria("createdOn", "<", measurementBeforeTimestamp.toString()));
			}

			// filter by sensor UUID
			if (isSet(sensorUUID)) {
				sensorU = new AlertSpecification(new SearchCriteria("sensorUUID", ":", sensorUUID.toString()));
			}

			// filter by temperature
			if (isSet(maxTemperature)) {
				highTmp = new AlertSpecification(new SearchCriteria("temperature", "<", maxTemperature));
			}
			if (isSet(minTemperature)) {
				lowTmp = new AlertSpecification(new SearchCriteria("temperature", ">", minTemperature));
			}

			if (Sensor.MASTER_UUID.equals(UUID)) {
				alerts = alertRepository.findAll(Specifications.where(highTmp).and(lowTmp).and(sensorU).and(afterTms).and(beforeTms));
				sensorsTable = sensorTableRepository.findAll();

				Map<String, SensorTable> sensorMap = new HashMap<>();
				Iterator<SensorTable> it = sensorsTable.iterator();

				while(it.hasNext()) {
					SensorTable tmp = it.next();
					sensorMap.put(tmp.getSensorUUID(), tmp);
				}

				for(Alert a : alerts)
					a.setName(sensorMap.get(a.getSensorUUID()).getName());

			} else {
				alerts = alertRepository.findAll(Specifications.where(highTmp).and(lowTmp).and(sensorU).and(afterTms).and(beforeTms));
				sensorsTable = sensorTableRepository.findBySensorUUID(UUID);

				SensorTable sensor = sensorsTable.iterator().next();

				for(Alert a : alerts)
					a.setName(sensor.getName());
			}

			return alerts;
		} else
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
	}

	private boolean isSet(String param) {
		return (param != null && !param.isEmpty());
	}

	@PostMapping(path = "/alerts")
	public @ResponseBody ResponseEntity<String> addNewIssue(@RequestBody Alert record,
			@RequestHeader(value = "UUID") String UUID) {
		logger.debug("Storing an alert.");
		if (Sensor.isUUIDValid(UUID)) {
			Alert newMeasurement = new Alert((record.getTimestamp() == null) ? null : record.getTimestamp(),
					(record.getSensorUUID() == null) ? null : record.getSensorUUID(),
					(record.getTemperature() == null) ? null : record.getTemperature(),
					(record.getHighTemperature()== null) ? null : record.getHighTemperature(),
					(record.getLowTemperature() == null) ? null : record.getLowTemperature());
			alertRepository.save(newMeasurement);
			return new ResponseEntity<>("Alert stored.", HttpStatus.OK);
		} else {
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
		}
	}

	@GetMapping(path = "/alerts/{id}")
	public @ResponseBody Alert getById(@PathVariable Long id, @RequestHeader(value = "UUID") String UUID) {
		logger.debug("Getting a single alert (id = {}).", id);

		if (Sensor.isUUIDValid(UUID)) {
			if (alertRepository.findOne(id).getSensorUUID().equals(UUID) || UUID.equals(Sensor.MASTER_UUID)) {
				return alertRepository.findOne(id);
			} else {
				throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidMeasurementUUID);
			}
		} else {
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
		}
	}

	@PutMapping("/alerts/{id}")
	public @ResponseBody Alert updateById(@PathVariable Long id, @RequestBody Alert record,
			@RequestHeader(value = "UUID") String UUID) {
		logger.debug("Updating an alert (id = {}).", id);
		if (Sensor.isUUIDValid(UUID)) {
			if (alertRepository.findOne(id).getSensorUUID().equals(UUID) || UUID.equals(Sensor.MASTER_UUID)) {
				Alert recordToUpdate = alertRepository.findOne(id);
				if (UUID.equals(Sensor.MASTER_UUID)) {
					recordToUpdate.updateFrom(record, true);
				}
				recordToUpdate.updateFrom(record, false);
				return alertRepository.save(recordToUpdate);
			} else {
				throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidMeasurementUUID);
			}
		} else {
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
		}
	}

	@DeleteMapping(path = "/alerts/{id}")
	public @ResponseBody ResponseEntity<String> deleteById(@PathVariable Long id,
			@RequestHeader(value = "UUID") String UUID) {
		logger.debug("Deleting an alert (id = {}).", id);
		if (Sensor.isUUIDValid(UUID)) {
			if (alertRepository.findOne(id).getSensorUUID().equals(UUID) || UUID.equals(Sensor.MASTER_UUID)) {
				alertRepository.delete(id);
				return new ResponseEntity<>("Alert deleted.", HttpStatus.OK);
			} else {
				throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidMeasurementUUID);
			}
		} else {
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
		}
	}

}