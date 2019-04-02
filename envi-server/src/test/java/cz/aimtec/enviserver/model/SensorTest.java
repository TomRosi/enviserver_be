package cz.aimtec.enviserver.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SensorTest {

	@Test
	public void validUUIDTest() {
		String uuid = "fc8ba70e-5936-4b44-b528-9e3b353405d9";
		assertTrue("UUID " + uuid + " shoud be valid.", Sensor.isUUIDValid(uuid));		
	}
	
	@Test
	public void invalidUUIDTest() {
		
		String uuid = "fa8fa79e-5234-3a3a-b528-6e3775ab0001";
		assertFalse("UUID " + uuid + " shoud be invalid.", Sensor.isUUIDValid(uuid));
	}

}
