package kr.hhplus.be.server.api.exception;

import jakarta.persistence.EntityNotFoundException;
import kr.hhplus.be.server.api.service.common.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResult> handleEntityNotFoundException(EntityNotFoundException ex) {
		ErrorResult errorResult = new ErrorResult(HttpStatus.NOT_FOUND.value(),
			ex.getMessage());
		return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResult> handleIllegalArgumentException(
		IllegalArgumentException ex) {
		ErrorResult errorResult = new ErrorResult(HttpStatus.BAD_REQUEST.value(),
			ex.getMessage());
		return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResult> handleGlobalException(Exception ex) {
		ErrorResult errorResult = new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(),
			ex.getMessage());
		return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}