package com.aiims.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErrorMessage {

    private int status;
    private LocalDateTime timestamp = LocalDateTime.now();
    private String code;
    private String message;

    public ErrorMessage(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
