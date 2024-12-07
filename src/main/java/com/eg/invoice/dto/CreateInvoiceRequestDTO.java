package com.eg.invoice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(name = "CreateInvoiceRequest", description = "Schema to hold Invoice information")
@AllArgsConstructor
public class CreateInvoiceRequestDTO {

	@Schema(description = "Invoice Amount", example = "1000.0")
	@NotNull(message = "Amount cannot be null")
	@DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
	private BigDecimal amount;

	@Schema(description = "Due date for the payment", example = "2024-12-31")
	@NotNull(message = "Due date cannot be null")
	@Future(message = "Due date must be a future date")
	private LocalDate dueDate;
}
