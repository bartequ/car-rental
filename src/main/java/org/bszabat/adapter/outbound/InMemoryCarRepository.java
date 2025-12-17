package org.bszabat.adapter.outbound;

import jakarta.annotation.PostConstruct;
import org.bszabat.application.CarRepository;
import org.bszabat.domain.Car;
import org.bszabat.domain.CarType;
import org.bszabat.domain.Reservation;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryCarRepository implements CarRepository {
    private final Map<UUID, Car> carStore = new ConcurrentHashMap<>();
    private final Map<UUID, List<Reservation>> reservationStore = new ConcurrentHashMap<>();

    @PostConstruct
    public void initData() {
        addCar(CarType.SEDAN, "KR-001");
        addCar(CarType.SEDAN, "KR-002");
        addCar(CarType.SUV, "KR-SUV1");
        addCar(CarType.VAN, "KR-VAN1");
    }

    private void addCar(CarType type, String plate) {
        UUID id = UUID.randomUUID();
        carStore.put(id, new Car(id, type, plate));
        reservationStore.put(id, new ArrayList<>());
    }

    @Override
    public List<Car> findCarsByType(CarType type) {
        return carStore.values().stream()
                .filter(car -> car.getType() == type)
                .toList();
    }

    @Override
    public List<Reservation> findReservationsByCar(Car car) {
        return reservationStore.getOrDefault(car.getId(), new ArrayList<>());
    }

    @Override
    public void saveReservation(Reservation reservation) {
        reservationStore.computeIfAbsent(reservation.getCar().getId(), k -> new ArrayList<>())
                .add(reservation);
    }

    @Override
    public List<Reservation> findAllReservations() {
        return reservationStore.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
