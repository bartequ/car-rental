package org.bszabat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Reservation {
    private final UUID reservationId;
    private final Car car;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return this.startDateTime.isBefore(otherEnd) && otherStart.isBefore(this.endDateTime);
    }
}
