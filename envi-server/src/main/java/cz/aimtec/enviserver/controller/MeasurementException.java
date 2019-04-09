package cz.aimtec.enviserver.controller;

import org.springframework.http.HttpStatus;

public class MeasurementException extends RuntimeException {
	
	private static final long serialVersionUID = -5384014698362003304L;
	public static final String invalidUUID = "Provided UUID isnt valid!";
	public static final String invalidMeasurementUUID = "Provided UUID isnt valid for this measurement, use valid UUID or master key";
	

	public MeasurementException() {
		super();
	}
	
	public MeasurementException(HttpStatus status) {
		super();
	}
	
	
	public MeasurementException(HttpStatus status, String reason, Throwable t) {
		super(reason);
	}
	
	public MeasurementException(HttpStatus status, String reason) {
		super(reason);
	}
}
