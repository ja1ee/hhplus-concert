package kr.hhplus.be.server.api.reservation.application.service;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationDto;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import kr.hhplus.be.server.api.reservation.application.event.ReservationConfirmedEvent;
import kr.hhplus.be.server.api.reservation.domain.entity.Reservation;
import kr.hhplus.be.server.api.reservation.domain.entity.ReservationOutbox;
import kr.hhplus.be.server.api.reservation.domain.repository.ReservationOutboxRepository;
import kr.hhplus.be.server.api.reservation.domain.repository.ReservationRepository;
import kr.hhplus.be.server.common.exception.CustomException;
import kr.hhplus.be.server.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static kr.hhplus.be.server.common.exception.ErrorCode.NOT_FOUND_RESERVATION;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationOutboxRepository reservationOutboxRepository;
	private final ApplicationEventPublisher applicationEventPublisher;

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

	@Transactional
	public ReservationResult confirmReservation(long id) {
		Reservation reservation = reservationRepository.findById(id);
		if (reservation.getExpiredAt().isBefore(LocalDateTime.now())) {
			throw new CustomException(ErrorCode.EXPIRED_SEAT);
		}
		reservation.confirm();
		ReservationResult result = ReservationResult.from(reservationRepository.save(reservation));
		applicationEventPublisher.publishEvent(new ReservationConfirmedEvent(result));

		return result;
	}

	public List<ReservationResult> getExpiredReservations() {
		List<Reservation> reservations = reservationRepository.findByExpiredAtBeforeAndIsReservedTrue(LocalDateTime.now());
		return reservations.stream()
				.map(ReservationResult::from)
				.collect(Collectors.toList());
	}

	public void expireReservation(ReservationResult result) {
		Reservation reservation = reservationRepository.findById(result.id());
		reservation.expire();
		reservationRepository.save(reservation);
	}

	public void publishRecord(ReservationConfirmedEvent eventPayload) {


		ReservationResult result = eventPayload.reservationResult();
		long reservationId = result.id();

		ReservationOutbox reservationOutbox = findOutboxById(reservationId);
		reservationOutbox.published();

		reservationOutboxRepository.save(reservationOutbox);
	}

	private ReservationOutbox findOutboxById(long reservationId) {
		return reservationOutboxRepository.findById(reservationId)
				.orElseThrow(() -> new CustomException(NOT_FOUND_RESERVATION));
	}
}
