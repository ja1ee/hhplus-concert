package kr.hhplus.be.server.api.service.reservation.domain;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import kr.hhplus.be.server.api.exception.ReservedSeatException;
import kr.hhplus.be.server.api.service.concert.application.dto.ConcertSeatDto;
import kr.hhplus.be.server.api.service.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.exception.ExpiredSeatsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;

	public Reservation makeReservation(ReservationDto dto) {
		Reservation existReservation = reservationRepository.findBySeatNoAndIsReservedTrue(
			dto.seatDto().seatNo());

		if (existReservation != null) {
			throw new ReservedSeatException("이미 예약된 좌석입니다.", existReservation);
		}

		Reservation reservation = dto.convertToEntity();
		reservation.setIsReserved(true);
		reservation.setExpiredAt(Instant.now().plus(Duration.ofMinutes(5)));

		return reservationRepository.save(reservation);
	}

	public Reservation checkIfReservationExpired(ConcertSeatDto seatDto) {
		Instant now = Instant.now();
		Reservation reservation = reservationRepository.findBySeatNo(seatDto.seatNo());
		if (reservation.getExpiredAt().isAfter(now)) {
			throw new ExpiredSeatsException("좌석 예약이 만료되었습니다.", reservation);
		}
		return reservation;
	}

	public Reservation confirmReservation(Reservation reservation) {
		reservation.setExpiredAt(null);
		return reservationRepository.save(reservation);
	}

}
