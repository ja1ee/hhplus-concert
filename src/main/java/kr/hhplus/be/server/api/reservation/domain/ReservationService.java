package kr.hhplus.be.server.api.reservation.domain;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

import kr.hhplus.be.server.api.common.exception.ReservedSeatException;
import kr.hhplus.be.server.api.concert.application.dto.ConcertSeatDto;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.common.exception.ExpiredSeatsException;
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

		ReservationGenerator generator = new ReservationGenerator(reservationRepository);
        return generator.generate(dto);
	}

	public Reservation confirmReservation(ConcertSeatDto seatDto) {
		Reservation reservation = reservationRepository.findBySeatNo(seatDto.seatNo());
		if (reservation.getExpiredAt().isAfter(LocalDateTime.now())) {
			throw new ExpiredSeatsException("좌석 예약이 만료되었습니다.", reservation);
		}
		reservation.confirm();
		reservationRepository.save(reservation);
		return reservation;
	}

}
