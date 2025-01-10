package kr.hhplus.be.server.api.service.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResult {

	private int status;

	private String message;
}
