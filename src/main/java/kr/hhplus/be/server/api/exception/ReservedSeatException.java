package kr.hhplus.be.server.api.exception;

import kr.hhplus.be.server.api.service.reservation.domain.Reservation;
import lombok.Getter;

@Getter
public class ReservedSeatException extends RuntimeException {

	private final Reservation reservation;

	public ReservedSeatException(String message, Reservation reservation) {
		super(message);
		this.reservation = reservation;
	}

}