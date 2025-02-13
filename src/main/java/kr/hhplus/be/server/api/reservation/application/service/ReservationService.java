package kr.hhplus.be.server.api.reservation.application.service;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.reservation.domain.entity.Reservation;
import kr.hhplus.be.server.api.reservation.domain.repository.ReservationRepository;
import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;

	public ReservationResult makeReservation(ReservationDto dto) {
		Reservation reservation = Reservation.builder().
				userId(dto.userId()).
				seatId(dto.seatId()).
				seatNo(dto.seatNo()).
				concertDate(dto.concertDate()).
				finalPrice(dto.finalPrice()).
				isReserved(true).
				expiredAt(LocalDateTime.now().plusMinutes(5)).
				build();
        return ReservationResult.from(reservationRepository.save(reservation));
	}

	public ReservationResult confirmReservation(long id) {
		Reservation reservation = reservationRepository.findById(id);
		if (reservation.getExpiredAt().isBefore(LocalDateTime.now())) {
			throw new CustomException(ErrorCode.EXPIRED_SEAT);
		}
		reservation.confirm();
		return ReservationResult.from(reservationRepository.save(reservation));
	}

	public List<Reservation> getExpiredReservations() {
		return reservationRepository.findByExpiredAtBeforeAndIsReservedTrue(LocalDateTime.now());
	}

	public void expireReservation(Reservation reservation) {
		reservation.expire();
		reservationRepository.save(reservation);
	}
}
