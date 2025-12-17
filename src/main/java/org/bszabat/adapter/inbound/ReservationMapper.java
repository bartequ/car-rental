package org.bszabat.adapter.inbound;

import org.bszabat.application.ReserveCarCommand;
import org.bszabat.domain.CarType;
import org.bszabat.domain.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "carType", source = "carType", qualifiedByName = "stringToCarType")
    ReserveCarCommand toCommand(ReservationRequest request);

    @Named("stringToCarType")
    default CarType mapStringToCarType(String carType) {
        if (carType == null || carType.isBlank()) {
            throw new IllegalArgumentException("Car type cannot be empty");
        }
        try {
            return CarType.valueOf(carType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid car type: " + carType);
        }
    }

    @Mapping(target = "licensePlate", source = "reservation.car.licensePlate")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "reservationId", source = "reservation.reservationId")
    ReservationResponse toResponse(Reservation reservation, String message);

    @Mapping(target = "licensePlate", source = "car.licensePlate")
    @Mapping(target = "message", constant = "Fetched")
    @Mapping(target = "reservationId", source = "reservationId")
    ReservationResponse mapToDto(Reservation reservation);

    List<ReservationResponse> toResponseList(List<Reservation> reservations);
}
