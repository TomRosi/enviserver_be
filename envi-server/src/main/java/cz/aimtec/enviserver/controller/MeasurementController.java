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

import cz.aimtec.enviserver.model.Measurement;
import cz.aimtec.enviserver.model.MeasurementStatus;
import cz.aimtec.enviserver.model.Sensor;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MeasurementController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MeasurementRepository measurementRepository;

	@GetMapping(path = "/measurements")
	public @ResponseBody Iterable<Measurement> getAllIssues(@RequestParam(required = false) String afterTimestamp,
			@RequestParam(required = false) String beforeTimestamp, @RequestParam(required = false) String sensorUUID,
			@RequestParam(required = false) String minTemperature,
			@RequestParam(required = false) String maxTemperature, @RequestParam(required = false) String status,
			@RequestParam(required = false) Integer maxResults, @RequestParam(required = false) Integer offset,
			@RequestHeader(value = "UUID") String UUID) {

		logger.debug("Fetching measurements.");

		if (Sensor.isUUIDValid(UUID)) {

			if (UUID.equals(Sensor.MASTER_UUID)) {
				Stream<Measurement> stream = StreamSupport.stream(measurementRepository.findAll().spliterator(), true);
				// filter by creation timestamp
				if (isSet(afterTimestamp)) {
					Timestamp measurementAfterTimestamp = Timestamp.valueOf(afterTimestamp);
					stream = stream.filter(item -> item.getTimestamp().after(measurementAfterTimestamp));
				}
				if (isSet(beforeTimestamp)) {
					Timestamp measurementBeforeTimestamp = Timestamp.valueOf(beforeTimestamp);
					stream = stream.filter(item -> item.getTimestamp().before(measurementBeforeTimestamp));
				}

				// filter by sensor UUID
				if (isSet(sensorUUID)) {
					stream = stream.filter(item -> item.getSensorUUID() == sensorUUID);
				}

				// filter by temperature
				if (isSet(maxTemperature)) {
					Float measurementMaxTemperature = Float.valueOf(maxTemperature);
					stream = stream.filter(item -> item.getTemperature() <= measurementMaxTemperature);
				}
				if (isSet(minTemperature)) {
					Float measurementMinTemperature = Float.valueOf(minTemperature);
					stream = stream.filter(item -> item.getTemperature() >= measurementMinTemperature);
				}

				// filter by status
				if (isSet(status)) {
					MeasurementStatus measurementStatus = MeasurementStatus.valueOf(status.toUpperCase());
					stream = stream.filter(item -> item.getStatus() == measurementStatus);
				}

				return stream.collect(Collectors.toList());

			} else {

				Stream<Measurement> stream = StreamSupport
						.stream(measurementRepository.findBySensorUUID(UUID).spliterator(), true);

				// filter by creation timestamp
				if (isSet(afterTimestamp)) {
					Timestamp measurementAfterTimestamp = Timestamp.valueOf(afterTimestamp);
					stream = stream.filter(item -> item.getTimestamp().after(measurementAfterTimestamp));
				}
				if (isSet(beforeTimestamp)) {
					Timestamp measurementBeforeTimestamp = Timestamp.valueOf(beforeTimestamp);
					stream = stream.filter(item -> item.getTimestamp().before(measurementBeforeTimestamp));
				}
				// filter by temperature
				if (isSet(maxTemperature)) {
					Float measurementMaxTemperature = Float.valueOf(maxTemperature);
					stream = stream.filter(item -> item.getTemperature() <= measurementMaxTemperature);
				}
				if (isSet(minTemperature)) {
					Float measurementMinTemperature = Float.valueOf(minTemperature);
					stream = stream.filter(item -> item.getTemperature() >= measurementMinTemperature);
				}

				// filter by status
				if (isSet(status)) {
					MeasurementStatus measurementStatus = MeasurementStatus.valueOf(status.toUpperCase());
					stream = stream.filter(item -> item.getStatus() == measurementStatus);
				}
				return stream.collect(Collectors.toList());
			}

		}
		Stream<Measurement> stream = StreamSupport.stream(null, true);
		return stream.collect(Collectors.toList());
	}

	private boolean isSet(String param) {
		return (param != null && !param.isEmpty());
	}

	@PostMapping(path = "/measurements")
	public @ResponseBody ResponseEntity<String> addNewIssue(@RequestBody Measurement record, @RequestHeader(value = "UUID") String UUID) {
		logger.debug("Storing a measurement.");
		if (Sensor.isUUIDValid(UUID)) {
		Measurement newMeasurement = new Measurement((record.getTimestamp() == null) ? null : record.getTimestamp(),
				(record.getSensorUUID() == null) ? null : record.getSensorUUID(),
				(record.getTemperature() == null) ? null : record.getTemperature(),
				(record.getStatus() == null) ? MeasurementStatus.OK : record.getStatus());
		measurementRepository.save(newMeasurement);
		return new ResponseEntity<>("Measurement stored.", HttpStatus.OK);
		}
		else {
			return null;
		}
	}

	@GetMapping(path = "/measurements/{id}")
	public @ResponseBody Measurement getById(@PathVariable Long id, @RequestHeader(value = "UUID") String UUID) {
		logger.debug("Getting a single measurement (id = {}).", id);
		if (Sensor.isUUIDValid(UUID)) {
			if (measurementRepository.findOne(id).getSensorUUID().equals(UUID) || UUID.equals(Sensor.MASTER_UUID)) {
				return measurementRepository.findOne(id);
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	@PutMapping("/measurements/{id}")
	public @ResponseBody Measurement updateById(@PathVariable Long id, @RequestBody Measurement record,
			@RequestHeader(value = "UUID") String UUID) {
		logger.debug("Updating a measurement (id = {}).", id);
		if (Sensor.isUUIDValid(UUID)) {
			if (measurementRepository.findOne(id).getSensorUUID().equals(UUID) || UUID.equals(Sensor.MASTER_UUID)) {
				Measurement recordToUpdate = measurementRepository.findOne(id);
				recordToUpdate.updateFrom(record);
				return measurementRepository.save(recordToUpdate);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@DeleteMapping(path = "/measurements/{id}")
	public @ResponseBody ResponseEntity<String> deleteById(@PathVariable Long id,
			@RequestHeader(value = "UUID") String UUID) {
		logger.debug("Deleting a measurement (id = {}).", id);
		if (Sensor.isUUIDValid(UUID)) {
			if (measurementRepository.findOne(id).getSensorUUID().equals(UUID) || UUID.equals(Sensor.MASTER_UUID)) {
				measurementRepository.delete(id);
				return new ResponseEntity<>("Measurement deleted.", HttpStatus.OK);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}