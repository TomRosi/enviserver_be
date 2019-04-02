package cz.aimtec.enviserver.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import cz.aimtec.enviserver.common.Helper;
import cz.aimtec.enviserver.model.Measurement;

@Controller    // This means that this class is a Controller
//RequestMapping(path="/setup") // This means URL's start with /issues (after Application path)
public class SetupController {
	
	private static final String DATA_FOLDER = "/cz/aimtec/enviserver/data/dummy/";
	
	Logger logger = LoggerFactory.getLogger(this.getClass());	
	
	@Autowired
	private MeasurementRepository measurementRepository;

	@GetMapping(path="/setup")
	public @ResponseBody String setup() {
		logger.debug("Setting up the database.");
		fillDatabaseStructures();
		return "Set-up.";
	}
	
	@GetMapping(path="/clean")
	public @ResponseBody String clean() {
		logger.debug("Clean up the database.");
		cleanDatabaseStructures();
		return "Clean.";
	}

	private void cleanDatabaseStructures() {		
		measurementRepository.deleteAll();
	}

	public void fillDatabaseStructures() {
		try {
			// read the issues from the dummy exampleMeasurements.json file and store them in the DB
			String measurementsJson = Helper.readResource(DATA_FOLDER + "exampleMeasurements.json", StandardCharsets.UTF_8);
			List<Measurement> measurements = getMeasurementsFromJson(measurementsJson);
			measurementRepository.save(measurements);
		} catch (IOException e) {
			logger.error("Cannot persist the initial data to the DB.", e);
		}
	}

	public List<Measurement> getMeasurementsFromJson(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return Arrays.asList(mapper.readValue(json, Measurement[].class));
	}
	
}