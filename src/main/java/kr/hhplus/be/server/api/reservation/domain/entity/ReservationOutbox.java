package kr.hhplus.be.server.api.reservation.domain.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.api.reservation.application.dto.ReservationResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Entity
@Slf4j
@NoArgsConstructor
public class ReservationOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventType;
    private String payload;
    private LocalDateTime createdAt;
    private boolean published;

    public ReservationOutbox(String eventType, ReservationResult payload, LocalDateTime createdAt, ObjectMapper objectMapper) {
        this.eventType = eventType;
        this.payload = serializePayload(payload, objectMapper);
        this.createdAt = createdAt;
    }

    public void published() {
        this.published = true;
    }

    private String serializePayload(ReservationResult payload, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("Error Processing Outbox Event", e);
            return null;
        }
    }

}
