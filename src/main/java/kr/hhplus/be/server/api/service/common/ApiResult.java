package kr.hhplus.be.server.api.service.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResult<T> {

	@Schema(description = "응답 메시지", example = "예약 가능한 날짜가 조회되었습니다.")
	private String message;

	@Schema(description = "응답 데이터")
	private T data;

	private ApiResult(String message, T data) {
		this.message = message;
		this.data = data;
	}

	public static <T> ApiResult<T> of(String message, T data) {
		return new ApiResult<>(message, data);
	}
}
