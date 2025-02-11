package kr.hhplus.be.server.api.queue.application.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record TokenStatusResult(
        Boolean isActivated,
        Long rank
) {
    public static TokenStatusResult from(List<Object> results) {
        return new TokenStatusResult(
                (Boolean) results.get(0),
                (Long) results.get(1)
        );
    }
}