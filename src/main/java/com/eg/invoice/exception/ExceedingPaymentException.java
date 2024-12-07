package com.eg.invoice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ExceedingPaymentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExceedingPaymentException(String message) {
        super(message);
    }
}