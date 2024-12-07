package com.eg.invoice.service;

import java.util.List;

import com.eg.invoice.dto.CreateInvoiceRequestDTO;
import com.eg.invoice.dto.CreateInvoiceResponseDTO;
import com.eg.invoice.dto.InvoiceResponseDTO;
import com.eg.invoice.dto.PaymentRequestDTO;
import com.eg.invoice.dto.ProcessOverdueRequestDTO;

public interface IInvoiceService {

    /**
     *
     * @param createInvoiceRequestDTO - Invoice creation request object
     * @return Id of the created invoice
     */
    public CreateInvoiceResponseDTO createInvoice(CreateInvoiceRequestDTO createInvoiceRequestDTO);

    /**
     *
     * @return All the invoices
     */
    public List<InvoiceResponseDTO> getAllInvoices();

    /**
     *
     * @param id                - Id of the invoice to be paid
     * @param paymentRequestDTO - Invoice payment object
     */
    public void payInvoice(Long id, PaymentRequestDTO paymentRequestDTO);

    /**
     *
     * @param processOverdueRequestDTO - Object for processing overdue invoices
     */
    public void processOverdueInvoices(ProcessOverdueRequestDTO processOverdueRequestDTO);
}
