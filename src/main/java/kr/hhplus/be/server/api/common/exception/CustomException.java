package kr.hhplus.be.server.api.common.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getReason());
		this.errorCode = errorCode;
		log.warn("CustomException 발생: {} - {}", errorCode, errorCode.getReason());
	}

}
