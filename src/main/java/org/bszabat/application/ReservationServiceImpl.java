package org.bszabat.application;

import org.bszabat.domain.Car;
import org.bszabat.domain.CarUnavailableException;
import org.bszabat.domain.Reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final CarRepository carRepository;

    @Override
    public Reservation reserveCar(ReserveCarCommand command) {
        LocalDateTime start = command.getStartDateTime();
        LocalDateTime end = start.plusDays(command.getDays());

        List<Car> candidates = carRepository.findCarsByType(command.getCarType());
        Car availableCar = candidates.stream()
                .filter(car -> isCarAvailable(car, start, end))
                .findFirst()
                .orElseThrow(() -> new CarUnavailableException(
                        String.format("No available %s for selected dates", command.getCarType())
                ));

        Reservation reservation = new Reservation(UUID.randomUUID(), availableCar, start, end);
        carRepository.saveReservation(reservation);

        return reservation;
    }

    @Override
    public List<Reservation> getAllReservations() {
        return carRepository.findAllReservations();
    }

    private boolean isCarAvailable(Car car, LocalDateTime start, LocalDateTime end) {
        List<Reservation> existingReservations = carRepository.findReservationsByCar(car);

        return existingReservations.stream()
                .noneMatch(res -> res.overlaps(start, end));
    }
}
