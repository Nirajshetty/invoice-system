package com.eg.invoice.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(name = "ProcessOverdueRequest", description = "Schema for processing overdue payments, containing details about late fees and overdue days.")
@AllArgsConstructor
public class ProcessOverdueRequestDTO {

        @Schema(description = "Late fee amount applied for overdue processing. It Must be a positive value.", example = "50.00", type = "number", format = "decimal")
        @NotNull(message = "Late fee cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Late fee must be greater than zero")
        private BigDecimal lateFee;

        @Schema(description = "Number of days the payment is overdue. Must be 1 or greater.", example = "10", type = "integer", format = "int32")
        @Min(value = 1, message = "Overdue days must be at least 1")
        private int overdueDays;
}
