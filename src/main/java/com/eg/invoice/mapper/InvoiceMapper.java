package com.eg.invoice.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eg.invoice.dto.CreateInvoiceResponseDTO;
import com.eg.invoice.dto.InvoiceResponseDTO;
import com.eg.invoice.entity.Invoice;

public class InvoiceMapper {

	public static CreateInvoiceResponseDTO mapToCreateInvoiceResponseDTO(Invoice invoice,
			CreateInvoiceResponseDTO createInvoiceResponseDTO) {
		createInvoiceResponseDTO.setId(invoice.getId());
		return createInvoiceResponseDTO;
	}

	public static InvoiceResponseDTO mapToInvoiceResponseDTO(Invoice invoice, InvoiceResponseDTO invoiceResponseDTO) {
		invoiceResponseDTO.setId(invoice.getId());
		invoiceResponseDTO.setAmount(invoice.getAmount());
		invoiceResponseDTO.setDueDate(invoice.getDueDate());
		invoiceResponseDTO.setPaidAmount(invoice.getPaidAmount());
		invoiceResponseDTO.setStatus(invoice.getStatus());
		return invoiceResponseDTO;
	}

	public static List<InvoiceResponseDTO> mapToInvoiceResponseDTOList(List<Invoice> invoices) {
		return invoices.stream()
				.map(invoice -> mapToInvoiceResponseDTO(invoice, new InvoiceResponseDTO()))
				.collect(Collectors.toList());
	}
}
