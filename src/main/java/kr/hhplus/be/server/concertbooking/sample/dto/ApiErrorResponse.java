package kr.hhplus.be.server.concertbooking.sample.dto;

import lombok.*;
@Getter
@AllArgsConstructor
@Builder
public class ApiErrorResponse {
	// http 코드는 헤더에 포함
	private String code; // 에러 코드
}