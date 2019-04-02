package cz.aimtec.enviserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such measurement.")  // 404
public class MeasurementNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -5384014698362003304L;

	public MeasurementNotFoundException() {
		super();
	}
	
	public MeasurementNotFoundException(Throwable t) {
		super(t);
	}
	
	public MeasurementNotFoundException(String message) {
		super(message);
	}
	
	public MeasurementNotFoundException(String message, Throwable t) {
		super(message, t);
	}
}
