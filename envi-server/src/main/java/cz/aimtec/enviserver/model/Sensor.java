package cz.aimtec.enviserver.model;

import java.util.HashSet;

public class Sensor {

	private static final HashSet<String> VALID_SENSORS = new HashSet<>();
	public static final String MASTER_UUID = "fc8ba70e-5936-4b44-b528-9e3b353405d9";
	
	static {
		VALID_SENSORS.add("df6021f7-3bdb-4ecb-9ccd-a0b50eeb141d");
		VALID_SENSORS.add("7f8e00ac-513e-4091-ba3c-c92aa80a3dfc");
		VALID_SENSORS.add("d384a529-6227-4133-afc9-4f5a16665f1f");
		VALID_SENSORS.add("353e2362-932a-442c-9518-bd0017b5f630");
		VALID_SENSORS.add("d7755342-65cc-4cd3-a6b4-ae0a495c3885");
		VALID_SENSORS.add("6133c3bb-a48e-43a4-add7-d11cd0cc7c93");
		VALID_SENSORS.add("10e65623-a843-488c-8beb-1beef2a7acca");
		VALID_SENSORS.add("71b11cb2-62ed-43b3-aa0e-6c81b5130707");
		VALID_SENSORS.add("650eb222-3078-40e7-9b38-d3291099d8ca");
		VALID_SENSORS.add("b5877e43-29eb-411a-bd1d-5aa9ea2d3d1f");
		VALID_SENSORS.add("41a3c8cf-3837-4070-9f0c-01bc1e442939");
		VALID_SENSORS.add("9a082e72-4876-4bf9-8ad7-76d5e80f0a74");
		VALID_SENSORS.add("1f675ef4-0137-4ecb-9f61-9e6562a3ff34");
		VALID_SENSORS.add("1321b437-b30b-4008-a599-97eec015d4aa");
		VALID_SENSORS.add("8d4c7344-3a32-4bd9-8650-5f418b1e0a11");
		VALID_SENSORS.add("736763a5-a71d-4db9-9c99-318c6c2ffd31");
		VALID_SENSORS.add("b159c6a0-0753-41e0-afbb-85b1cb9f2c6f");
		VALID_SENSORS.add("fdf35a23-dd0d-4a64-966e-a2e89a09b0ff");
		VALID_SENSORS.add("610eb3d2-d48d-47b0-ba4e-5e04f5c55c80");
	}
	
	public static final boolean isUUIDValid(String uuid) {
		return VALID_SENSORS.contains(uuid) || MASTER_UUID.equals(uuid);
	}
	
	public static final String[] getAllSensors () {
		return VALID_SENSORS.toArray(new String[0]);
	}

	public Sensor() {

	}
}
