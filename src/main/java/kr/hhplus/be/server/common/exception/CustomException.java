package kr.hhplus.be.server.common.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getReason());
		this.errorCode = errorCode;
		log.warn("CustomException 발생: {} - {}", errorCode, errorCode.getReason());
	}

}
