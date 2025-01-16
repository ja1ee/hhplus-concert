package kr.hhplus.be.server.api.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {

	@Schema(description = "응답 메시지", example = "예약 가능한 날짜가 조회되었습니다.")
	private String message;

	@Schema(description = "응답 데이터")
	private T data;

	private ApiResponse(String message, T data) {
		this.message = message;
		this.data = data;
	}

	public static <T> ApiResponse<T> of(String message, T data) {
		return new ApiResponse<>(message, data);
	}
}
