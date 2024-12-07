package com.eg.invoice.service;

import com.eg.invoice.dto.*;
import com.eg.invoice.entity.Invoice;
import com.eg.invoice.enums.PaymentStatus;
import com.eg.invoice.exception.ExceedingPaymentException;
import com.eg.invoice.repository.InvoiceRepository;
import com.eg.invoice.service.impl.InvoiceServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createInvoice_ShouldSaveInvoiceAndReturnResponse() {

        CreateInvoiceRequestDTO request = new CreateInvoiceRequestDTO(new BigDecimal("100.00"),
                LocalDate.now().plusDays(10));
        Invoice savedInvoice = new Invoice(1L, new BigDecimal("100.00"), BigDecimal.ZERO, request.getDueDate(),
                PaymentStatus.PENDING);

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(savedInvoice);

        CreateInvoiceResponseDTO response = invoiceService.createInvoice(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    public void getAllInvoices_ShouldReturnListOfInvoices() {

        Invoice invoice1 = new Invoice(1L, new BigDecimal("100.00"), new BigDecimal("50.00"), LocalDate.now(),
                PaymentStatus.PENDING);
        Invoice invoice2 = new Invoice(2L, new BigDecimal("200.00"), new BigDecimal("100.00"),
                LocalDate.now().plusDays(10), PaymentStatus.PAID);

        when(invoiceRepository.findAll()).thenReturn(Arrays.asList(invoice1, invoice2));

        var result = invoiceService.getAllInvoices();

        assertEquals(2, result.size());
        assertEquals(PaymentStatus.PENDING, result.get(0).getStatus());
        verify(invoiceRepository, times(1)).findAll();
    }

    @Test
    public void payInvoice_ShouldUpdateInvoice_WhenPaymentIsValid() {

        Invoice invoice = new Invoice(1L, new BigDecimal("100.00"), new BigDecimal("50.00"), LocalDate.now(),
                PaymentStatus.PENDING);
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(new BigDecimal("50.00"));

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        invoiceService.payInvoice(1L, paymentRequest);

        assertEquals(new BigDecimal("100.00"), invoice.getPaidAmount());
        assertEquals(PaymentStatus.PAID, invoice.getStatus());
        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test
    public void payInvoice_ShouldThrowException_WhenPaymentExceedsInvoiceAmount() {

        Invoice invoice = new Invoice(1L, new BigDecimal("100.00"), new BigDecimal("50.00"), LocalDate.now(),
                PaymentStatus.PENDING);
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(new BigDecimal("60.00"));

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        ExceedingPaymentException exception = assertThrows(
                ExceedingPaymentException.class,
                () -> invoiceService.payInvoice(1L, paymentRequest));
        assertEquals("Payment exceeds the invoice amount by 10.00. Balance amount to be paid is 50.00",
                exception.getMessage());
        verify(invoiceRepository, never()).save(any(Invoice.class));
    }

    @Test
    public void processOverdueInvoices_ShouldCreateNewInvoicesForOverduePayments() {

        Invoice overdueInvoice = new Invoice(1L, new BigDecimal("100.00"), BigDecimal.ZERO,
                LocalDate.now().minusDays(10), PaymentStatus.PENDING);
        ProcessOverdueRequestDTO request = new ProcessOverdueRequestDTO(new BigDecimal("10.00"), 5);

        when(invoiceRepository.findAll()).thenReturn(Arrays.asList(overdueInvoice));

        invoiceService.processOverdueInvoices(request);

        verify(invoiceRepository, times(2)).save(any(Invoice.class));
    }

    @Test
    public void findInvoiceById_ShouldReturnInvoice_WhenIdIsValid() {

        Invoice invoice = new Invoice(1L, new BigDecimal("100.00"), new BigDecimal("50.00"), LocalDate.now(),
                PaymentStatus.PENDING);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        Invoice result = invoiceService.findInvoiceById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void findInvoiceById_ShouldThrowException_WhenIdIsInvalid() {

        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> invoiceService.findInvoiceById(1L));
        assertEquals("Invoice not found with id: 1", exception.getMessage());
    }
}
