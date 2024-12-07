package com.eg.invoice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.eg.invoice.enums.PaymentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(name = "InvoiceResponse", description = "Schema to hold Invoice information")
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDTO {

    @Schema(description = "Unique identifier for the invoice", example = "1234", type = "integer", format = "int64")
    @NotNull(message = "ID cannot be null")
    private Long id;

    @Schema(description = "Total amount to be paid.", example = "199.99", type = "number", format = "decimal")
    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Schema(description = "Amount already paid.", example = "0.00", type = "number", format = "decimal")
    @NotNull(message = "Paid amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Paid amount must be zero or greater")
    private BigDecimal paidAmount;

    @Schema(description = "Due date for the payment.", example = "2021-09-11", type = "string", format = "date")
    @NotNull(message = "Due date cannot be null")
    private LocalDate dueDate;

    @Schema(description = "Status of the payment. Valid values are: pending, paid, void.", example = "pending")
    @NotNull(message = "Status cannot be null")
    private PaymentStatus status;
}
