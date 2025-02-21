package kr.hhplus.be.server.api.reservation.application.service;

import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;

public interface MessageProducer {
    void send(String topic, ReservationResult message);
}
