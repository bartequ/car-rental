package org.bszabat.application;

import org.bszabat.domain.Car;
import org.bszabat.domain.CarType;
import org.bszabat.domain.CarUnavailableException;
import org.bszabat.domain.Reservation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Car sedan;

    @BeforeEach
    void setup() {
        sedan = Car.builder()
                .id(UUID.randomUUID())
                .type(CarType.SEDAN)
                .licensePlate("WA-001")
                .build();
    }

    @Test
    @DisplayName("Should reserve car successfully when fleet is available")
    void shouldReserveCarSuccessfully() {
        // Given
        ReserveCarCommand command = ReserveCarCommand.builder()
                .carType(CarType.SEDAN)
                .startDateTime(LocalDateTime.now().plusDays(1))
                .days(3)
                .build();

        when(carRepository.findCarsByType(CarType.SEDAN)).thenReturn(List.of(sedan));
        when(carRepository.findReservationsByCar(sedan)).thenReturn(Collections.emptyList());

        // When
        Reservation result = reservationService.reserveCar(command);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(sedan, result.getCar());
        verify(carRepository).saveReservation(any(Reservation.class));
    }

    @Test
    @DisplayName("Should throw exception when fleet is empty for given type")
    void shouldThrowExceptionWhenNoCarsOfTypeExist() {
        // Given
        ReserveCarCommand command = ReserveCarCommand.builder()
                .carType(CarType.VAN)
                .startDateTime(LocalDateTime.now().plusDays(1))
                .days(2)
                .build();

        when(carRepository.findCarsByType(CarType.VAN)).thenReturn(Collections.emptyList());

        // When & Then
        CarUnavailableException exception = Assertions.assertThrows(CarUnavailableException.class, () -> {
            reservationService.reserveCar(command);
        });

        Assertions.assertEquals("No available VAN for selected dates", exception.getMessage());
        verify(carRepository, never()).saveReservation(any());
    }

    @Test
    @DisplayName("Should throw exception when the only available car is booked during requested dates")
    void shouldThrowExceptionWhenLastAvailableCarOverlaps() {
        // Given
        LocalDateTime requestStart = LocalDateTime.of(2023, 10, 10, 10, 0);
        int requestDays = 2;

        ReserveCarCommand command = ReserveCarCommand.builder()
                .carType(CarType.SEDAN)
                .startDateTime(requestStart)
                .days(requestDays)
                .build();

        Reservation existingReservation = new Reservation(
                UUID.randomUUID(),
                sedan,
                LocalDateTime.of(2023, 10, 11, 10, 0),
                LocalDateTime.of(2023, 10, 13, 10, 0)
        );

        when(carRepository.findCarsByType(CarType.SEDAN)).thenReturn(List.of(sedan));
        when(carRepository.findReservationsByCar(sedan)).thenReturn(List.of(existingReservation));

        // When & Then
        CarUnavailableException exception = Assertions.assertThrows(CarUnavailableException.class, () -> {
            reservationService.reserveCar(command);
        });

        Assertions.assertEquals("No available SEDAN for selected dates", exception.getMessage());
        verify(carRepository, never()).saveReservation(any());
    }
}