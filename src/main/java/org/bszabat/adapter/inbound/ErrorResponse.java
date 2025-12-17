package org.bszabat.adapter.inbound;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private final String message;
    private String id;
}
