package com.eg.invoice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eg.invoice.dto.CreateInvoiceRequestDTO;
import com.eg.invoice.dto.CreateInvoiceResponseDTO;
import com.eg.invoice.dto.ErrorResponseDTO;
import com.eg.invoice.dto.InvoiceResponseDTO;
import com.eg.invoice.dto.PaymentRequestDTO;
import com.eg.invoice.dto.ProcessOverdueRequestDTO;
import com.eg.invoice.service.IInvoiceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;

@Tag(name = "REST APIs for Invoice system", description = "REST APIs for creating invoices, paying invoices, and processing overdue invoices")
@RestController
@RequestMapping(path = "/invoices", produces = { MediaType.APPLICATION_JSON_VALUE })
@AllArgsConstructor
@Validated
public class InvoiceController {

	private IInvoiceService iInvoiceService;

	@Operation(summary = "Create a new Invoice", description = "This endpoint is used to create a new invoice.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Invoice created successfully", content = @Content(schema = @Schema(implementation = CreateInvoiceResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	@PostMapping
	public ResponseEntity<CreateInvoiceResponseDTO> createInvoice(@Valid @RequestBody CreateInvoiceRequestDTO request) {
		CreateInvoiceResponseDTO createInvoiceResponseDTO = iInvoiceService.createInvoice(request);
		return ResponseEntity.status(201).body(createInvoiceResponseDTO);
	}

	@Operation(summary = "Get all invoices", description = "This endpoint retrieves a list of all invoices.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "List of all invoices", content = @Content(schema = @Schema(implementation = InvoiceResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	@GetMapping
	public ResponseEntity<List<InvoiceResponseDTO>> getAllInvoices() {
		List<InvoiceResponseDTO> invoices = iInvoiceService.getAllInvoices();
		return ResponseEntity.ok(invoices);
	}

	@Operation(summary = "Pay an invoice", description = "This endpoint allows a user to pay an invoice by providing the payment details.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Invoice paid successfully"),
			@ApiResponse(responseCode = "404", description = "Invoice not found", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	@PostMapping("/{id}/payments")
	public ResponseEntity<Void> payInvoice(@PathVariable Long id, @Valid @RequestBody PaymentRequestDTO request) {
		iInvoiceService.payInvoice(id, request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Process overdue invoices", description = "This endpoint processes invoices that are overdue, including applying late fees.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Overdue invoices processed successfully"),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	@PostMapping("/process-overdue")
	public ResponseEntity<Void> processOverdueInvoices(@Valid @RequestBody ProcessOverdueRequestDTO request) {
		iInvoiceService.processOverdueInvoices(request);
		return ResponseEntity.ok().build();
	}

}
