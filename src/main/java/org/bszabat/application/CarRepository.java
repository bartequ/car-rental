package org.bszabat.application;

import org.bszabat.domain.Car;
import org.bszabat.domain.CarType;
import org.bszabat.domain.Reservation;

import java.util.List;

public interface CarRepository {
    List<Car> findCarsByType(CarType type);

    List<Reservation> findReservationsByCar(Car car);

    void saveReservation(Reservation reservation);

    List<Reservation> findAllReservations();
}
