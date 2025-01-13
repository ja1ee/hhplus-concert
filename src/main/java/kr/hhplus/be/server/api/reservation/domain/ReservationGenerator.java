package kr.hhplus.be.server.api.reservation.domain;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReservationGenerator {

    private final ReservationRepository reservationRepository;

    @Transactional
    public Reservation generate(ReservationDto dto) {

        return reservationRepository.save(Reservation.builder().
                userId(dto.userId()).
                seatId(dto.seatDto().id()).
                seatNo(dto.seatDto().seatNo()).
                concertDate(dto.concertDate()).
                finalPrice(dto.finalPrice()).
                isReserved(true).
                expiredAt(LocalDateTime.now().plusMinutes(5)).
                build());
    }
}
