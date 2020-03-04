package cz.aimtec.enviserver.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.aimtec.enviserver.model.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.domain.Specifications;

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

import cz.aimtec.enviserver.controller.MeasurementException;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class MeasurementController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MeasurementRepository measurementRepository;

	@Autowired
	private SensorTableRepository sensorTableRepository;

	@GetMapping(path = "/measurements")
	public @ResponseBody Iterable<Measurement> getAllIssues(
			@RequestParam(required = false) String afterTimestamp,
			@RequestParam(required = false) String beforeTimestamp,
			@RequestParam(required = false) String sensorUUID,
			@RequestParam(required = false) String minTemperature,
			@RequestParam(required = false) String maxTemperature,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) Integer maxResults,
			@RequestParam(required = false) Integer offset,

			@RequestParam(required = false) String selectedUUID,

			@RequestHeader(value = "UUID") String UUID) {

		logger.debug("Fetching measurements.");

		Iterable<Measurement> measurements;
		Iterable<SensorTable> sensorsTable;


		if (Sensor.isUUIDValid(UUID)) {
			MeasurementSpecification sensor = null;
			MeasurementSpecification afterTms = null;
			MeasurementSpecification beforeTms = null;
			MeasurementSpecification maxTmp = null;
			MeasurementSpecification minTmp = null;
			MeasurementSpecification stat = null;

			if (isSet(afterTimestamp)) {
				Timestamp measurementAfterTimestamp = Timestamp.valueOf(afterTimestamp);
				afterTms = new MeasurementSpecification(
						new SearchCriteria("createdOn", ">", measurementAfterTimestamp.toString()));
			}
			if (isSet(beforeTimestamp)) {
				Timestamp measurementBeforeTimestamp = Timestamp.valueOf(beforeTimestamp);
				beforeTms = new MeasurementSpecification(
						new SearchCriteria("createdOn", "<", measurementBeforeTimestamp.toString()));
			}

			// filter by sensor UUID
			if (isSet(sensorUUID)) {
				sensor = new MeasurementSpecification(new SearchCriteria("sensorUUID", ":", sensorUUID.toString()));
			}

			// filter by temperature
			if (isSet(maxTemperature)) {
				maxTmp = new MeasurementSpecification(new SearchCriteria("temperature", "<", maxTemperature));
			}
			if (isSet(minTemperature)) {
				minTmp = new MeasurementSpecification(new SearchCriteria("temperature", ">", minTemperature));
				//http://localhost:8080/enviserver/measurements?maxTemperature=27&minTemperature=10
			}

			// filter by status
			if (isSet(status)) {
				stat = new MeasurementSpecification(new SearchCriteria("status", ":", status));
			}

			if (Sensor.MASTER_UUID.equals(UUID)) {

				measurements = measurementRepository.findAll(Specifications.where(maxTmp).and(minTmp).and(sensor).and(afterTms).and(stat).and(beforeTms));
				sensorsTable = sensorTableRepository.findAll();

				Map<String, SensorTable> sensorMap = new HashMap<>();
				Iterator<SensorTable> it = sensorsTable.iterator();

				while(it.hasNext()) {
					SensorTable tmp = it.next();
					sensorMap.put(tmp.getSensorUUID(), tmp);
				}

				for(Measurement m : measurements) {
					if (sensorMap.containsKey(m.getSensorUUID())) {
						m.setName(sensorMap.get(m.getSensorUUID()).getName());
					}
				}

			} else {
				measurements = measurementRepository.findAll(Specifications.where(maxTmp).and(minTmp).and(sensor).and(afterTms).and(stat).and(beforeTms));
				sensorsTable = sensorTableRepository.findBySensorUUID(UUID);

				SensorTable sensorT = sensorsTable.iterator().next();

				for(Measurement m : measurements)
					m.setName(sensorT.getName());
			}

			for(Measurement m : measurements)
				if(sensorTableRepository.findBySensorUUID(m.getSensorUUID()).iterator().hasNext())
					m.setName(sensorTableRepository.findBySensorUUID(m.getSensorUUID()).iterator().next().getName());

			return measurements;

		} else {
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
		}
	}

	private boolean isSet(String param) {
		return (param != null && !param.isEmpty());
	}

	@PostMapping(path = "/measurements")
	public @ResponseBody ResponseEntity<String> addNewIssue(@RequestBody Measurement record,
			@RequestHeader(value = "UUID") String UUID) {
		logger.debug("Storing a measurement.");
		if (Sensor.isUUIDValid(UUID)) {
			Measurement newMeasurement = new Measurement((record.getTimestamp() == null) ? null : record.getTimestamp(),
					(record.getSensorUUID() == null) ? null : record.getSensorUUID(),
					(record.getTemperature() == null) ? null : record.getTemperature(),
					(record.getStatus() == null) ? MeasurementStatus.OK : record.getStatus());

			newMeasurement = measurementRepository.save(newMeasurement);

			JSONObject aJsonObject = new JSONObject(newMeasurement);
			aJsonObject.put("message", "Measurement stored!");

			return new ResponseEntity<>(aJsonObject.toString(), HttpStatus.OK);
		} else {
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
		}
	}

	@GetMapping(path = "/measurements/{id}")
	public @ResponseBody Measurement getById(@PathVariable Long id, @RequestHeader(value = "UUID") String UUID) {
		logger.debug("Getting a single measurement (id = {}).", id);

		if (Sensor.isUUIDValid(UUID)) {
			if (measurementRepository.findOne(id).getSensorUUID().equals(UUID) || UUID.equals(Sensor.MASTER_UUID)) {
				return measurementRepository.findOne(id);
			} else {
				throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidMeasurementUUID);
			}
		} else {
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
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
				throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidMeasurementUUID);
			}
		} else {
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
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
				throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidMeasurementUUID);
			}
		} else {
			throw new MeasurementException(HttpStatus.BAD_REQUEST, MeasurementException.invalidUUID);
		}
	}

}