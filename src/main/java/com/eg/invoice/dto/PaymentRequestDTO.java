package com.eg.invoice.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(name = "PaymentRequest", description = "Schema to hold payment information")
@AllArgsConstructor
public class PaymentRequestDTO {

        @Schema(description = "Amount to be paid", example = "1000.0")
        @NotNull(message = "Amount cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
        private BigDecimal amount;
}
