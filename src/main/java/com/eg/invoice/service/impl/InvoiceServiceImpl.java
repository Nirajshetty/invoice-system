package com.eg.invoice.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eg.invoice.dto.CreateInvoiceRequestDTO;
import com.eg.invoice.dto.CreateInvoiceResponseDTO;
import com.eg.invoice.dto.InvoiceResponseDTO;
import com.eg.invoice.dto.PaymentRequestDTO;
import com.eg.invoice.dto.ProcessOverdueRequestDTO;
import com.eg.invoice.entity.Invoice;
import com.eg.invoice.enums.PaymentStatus;
import com.eg.invoice.exception.ExceedingPaymentException;
import com.eg.invoice.mapper.InvoiceMapper;
import com.eg.invoice.repository.InvoiceRepository;
import com.eg.invoice.service.IInvoiceService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements IInvoiceService {

    private InvoiceRepository invoiceRepository;

    @Override
    public CreateInvoiceResponseDTO createInvoice(CreateInvoiceRequestDTO createInvoiceRequestDTO) {
        Invoice invoice = new Invoice(createInvoiceRequestDTO.getAmount(), createInvoiceRequestDTO.getDueDate());
        Invoice createdInvoice = invoiceRepository.save(invoice);
        return InvoiceMapper.mapToCreateInvoiceResponseDTO(createdInvoice, new CreateInvoiceResponseDTO());
    }

    @Override
    public List<InvoiceResponseDTO> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return InvoiceMapper.mapToInvoiceResponseDTOList(invoices);
    }

    @Override
    public void payInvoice(Long id, PaymentRequestDTO paymentRequestDTO) {
        Invoice invoice = this.findInvoiceById(id);

        BigDecimal newPaidAmount = invoice.getPaidAmount().add(paymentRequestDTO.getAmount());
        if (newPaidAmount.compareTo(invoice.getAmount()) > 0) {
            throw new ExceedingPaymentException("Payment exceeds the invoice amount by "
                    + newPaidAmount.subtract(invoice.getAmount())
                    + ". Balance amount to be paid is "
                    + invoice.getAmount().subtract(invoice.getPaidAmount()));
        }

        invoice.setPaidAmount(newPaidAmount);
        if (newPaidAmount.compareTo(invoice.getAmount()) == 0) {
            invoice.setStatus(PaymentStatus.PAID);
        }

        invoiceRepository.save(invoice);
    }

    @Override
    public void processOverdueInvoices(ProcessOverdueRequestDTO processOverdueRequestDTO) {
        List<Invoice> invoices = invoiceRepository.findAll();
        for (Invoice invoice : invoices) {
            if (invoice.getStatus() == PaymentStatus.PENDING &&
                    invoice.getDueDate()
                            .isBefore(LocalDate.now().minusDays(processOverdueRequestDTO.getOverdueDays()))) {

                if (invoice.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
                    Invoice newInvoice = createRemainingInvoice(invoice, processOverdueRequestDTO.getLateFee());
                    invoice.setStatus(PaymentStatus.PAID);
                    invoiceRepository.save(newInvoice);
                } else {
                    Invoice newInvoice = createFullOverdueInvoice(invoice, processOverdueRequestDTO.getLateFee());
                    invoice.setStatus(PaymentStatus.VOID);
                    invoiceRepository.save(newInvoice);
                }

                invoiceRepository.save(invoice);
            }
        }
    }

    public Invoice createRemainingInvoice(Invoice original, BigDecimal lateFee) {
        BigDecimal remainingAmount = original.getAmount().subtract(original.getPaidAmount()).add(lateFee);
        return new Invoice(remainingAmount, LocalDate.now().plusDays(30));
    }

    public Invoice createFullOverdueInvoice(Invoice original, BigDecimal lateFee) {
        BigDecimal newAmount = original.getAmount().add(lateFee);
        return new Invoice(newAmount, LocalDate.now().plusDays(30));
    }

    public Invoice findInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with id: " + id));
    }

}
