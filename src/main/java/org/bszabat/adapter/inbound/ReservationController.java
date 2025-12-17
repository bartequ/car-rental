package org.bszabat.adapter.inbound;

import lombok.RequiredArgsConstructor;
import org.bszabat.application.ReservationService;
import org.bszabat.application.ReserveCarCommand;
import org.bszabat.domain.Reservation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationMapper mapper;

    @PostMapping
    public ReservationResponse createReservation(@RequestBody ReservationRequest request) {
        ReserveCarCommand command = mapper.toCommand(request);
        Reservation reservation = reservationService.reserveCar(command);

        return mapper.toResponse(reservation, "Reservation confirmed");
    }

    @GetMapping
    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();

        return mapper.toResponseList(reservations);
    }
}
