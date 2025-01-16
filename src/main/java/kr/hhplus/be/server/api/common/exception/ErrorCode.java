package kr.hhplus.be.server.api.common.exception;

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
    NOT_FOUND_SCHEDULE(HttpStatus.BAD_REQUEST, "존재하지 않는 콘서트입니다."),
    NOT_FOUND_SEAT(HttpStatus.BAD_REQUEST, "예약 가능한 좌석이 없습니다."),
    ALREADY_RESERVED_SEAT(HttpStatus.BAD_REQUEST, "이미 선점된 좌석입니다."),
    // User
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "잔액이 부족합니다."),
    INSUFFICIENT_CHARGE(HttpStatus.BAD_REQUEST, "잘못된 금액이 입력되었습니다.");



    private final HttpStatus httpStatus;
    private final String reason;
}