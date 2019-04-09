package cz.aimtec.enviserver.controller;

import java.sql.Timestamp;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.aimtec.enviserver.model.Alert;
import cz.aimtec.enviserver.model.Sensor;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AlertController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AlertRepository alertRepository;

	@GetMapping(path = "/alerts")
	public @ResponseBody Iterable<Alert> getAllIssues(@RequestHeader(value = "UUID") String UUID) {

		logger.debug("Fetching measurements.");

		if (Sensor.isUUIDValid(UUID)) {

			if (UUID.equals(Sensor.MASTER_UUID)) {
				Stream<Alert> stream = StreamSupport.stream(alertRepository.findAll().spliterator(), true);
				return stream.collect(Collectors.toList());

			} else {
				Stream<Alert> stream = StreamSupport.stream(alertRepository.findBySensorUUID(UUID).spliterator(), true);
				return stream.collect(Collectors.toList());
			}
		} else {
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
		}
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