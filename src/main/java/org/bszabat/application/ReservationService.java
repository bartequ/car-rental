package org.bszabat.application;

import org.bszabat.domain.Reservation;

import java.util.List;

public interface ReservationService {
    Reservation reserveCar(ReserveCarCommand command);

    List<Reservation> getAllReservations();
}
