package cz.aimtec.enviserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such Issue.")  // 404
public class IssueNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -5384014698362003304L;

	public IssueNotFoundException() {
		super();
	}
	
	public IssueNotFoundException(Throwable t) {
		super(t);
	}
	
	public IssueNotFoundException(String message) {
		super(message);
	}
	
	public IssueNotFoundException(String message, Throwable t) {
		super(message, t);
	}
}
