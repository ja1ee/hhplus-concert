package kr.hhplus.be.server.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Token
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "대기열 토큰이 만료되었습니다."),
    // Reservation
    EXPIRED_SEAT(HttpStatus.BAD_REQUEST, "좌석 예약이 만료되었습니다."),
    // Concert
    NOT_FOUND_SCHEDULE(HttpStatus.NOT_FOUND, "존재하지 않는 콘서트입니다."),
    NOT_FOUND_SEAT(HttpStatus.NOT_FOUND, "예약 가능한 좌석이 없습니다."),
    ALREADY_RESERVED_SEAT(HttpStatus.BAD_REQUEST, "이미 선점된 좌석입니다."),
    // User
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "잔액이 부족합니다."),
    INSUFFICIENT_CHARGE(HttpStatus.BAD_REQUEST, "잘못된 금액이 입력되었습니다."),

    INTERRUPT_OCCURRED(HttpStatus.BAD_REQUEST, "인터럽트가 발생했습니다"),
    FAILED_GET_LOCK(HttpStatus.BAD_REQUEST, "락 획득에 실패하였습니다.");

    private final HttpStatus httpStatus;
    // HttpStatus 를 필드로 가지면 서비스의 에러가 웹응답코드와 결합되지만 편리함을 위해 어느정도 허용됨.
    private final String reason;
}