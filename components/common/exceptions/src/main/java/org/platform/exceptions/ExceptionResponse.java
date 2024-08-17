package org.platform.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private String message;
    private String errorCode;
    private String errorDetail;
    private UUID conflictingID;
    private List<String> errorDetails;

    public ExceptionResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public ExceptionResponse(String errorCode, String message, List<String> errorDetails) {
        this.errorCode = errorCode;
        this.message = message;
        this.errorDetails = errorDetails;
    }
}