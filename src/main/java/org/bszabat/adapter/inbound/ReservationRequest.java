package org.bszabat.adapter.inbound;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequest {
    private String carType;
    private LocalDateTime startDateTime;
    private int days;
}
