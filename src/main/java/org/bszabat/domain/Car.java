package org.bszabat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Car {
    private final UUID id;
    private final CarType type;
    private final String licensePlate;
}
