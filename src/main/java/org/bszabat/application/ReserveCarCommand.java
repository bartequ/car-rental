package org.bszabat.application;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.bszabat.domain.CarType;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReserveCarCommand {
    @NonNull
    private final CarType carType;
    @NonNull
    private final LocalDateTime startDateTime;
    private final int days;
}
