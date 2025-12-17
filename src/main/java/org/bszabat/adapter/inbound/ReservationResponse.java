package org.bszabat.adapter.inbound;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ReservationResponse {
    private UUID reservationId;
    private String licensePlate;
    private String message;
}
