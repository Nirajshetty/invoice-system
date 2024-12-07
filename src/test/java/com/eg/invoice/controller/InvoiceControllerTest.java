package com.eg.invoice.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.eg.invoice.dto.CreateInvoiceResponseDTO;
import com.eg.invoice.dto.InvoiceResponseDTO;
import com.eg.invoice.enums.PaymentStatus;
import com.eg.invoice.exception.ExceedingPaymentException;
import com.eg.invoice.service.IInvoiceService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IInvoiceService invoiceService;

    @Test
    public void createInvoice_ShouldReturnCreatedInvoice() throws Exception {
        CreateInvoiceResponseDTO response = new CreateInvoiceResponseDTO(1L);

        Mockito.when(invoiceService.createInvoice(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100.00,\"dueDate\":\"2024-12-31\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void getAllInvoices_ShouldReturnInvoiceList() throws Exception {
        List<InvoiceResponseDTO> invoices = Arrays.asList(
                new InvoiceResponseDTO(1L, new BigDecimal("100.00"), new BigDecimal("50.00"), LocalDate.now(),
                        PaymentStatus.PENDING),
                new InvoiceResponseDTO(2L, new BigDecimal("200.00"), new BigDecimal("100.00"),
                        LocalDate.now().plusDays(10), PaymentStatus.PENDING));

        Mockito.when(invoiceService.getAllInvoices()).thenReturn(invoices);

        mockMvc.perform(get("/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount", is(100.00)))
                .andExpect(jsonPath("$[1].status", is("PENDING")));
    }

    @Test
    public void payInvoice_ShouldReturnStatusOk_WhenPaymentIsSuccessful() throws Exception {
        Mockito.doNothing().when(invoiceService).payInvoice(Mockito.anyLong(), Mockito.any());

        mockMvc.perform(post("/invoices/1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":50.00}"))
                .andExpect(status().isOk());
    }

    @Test
    public void payInvoice_ShouldReturnBadRequest_WhenPaymentExceedsInvoiceAmount() throws Exception {
        Mockito.doThrow(new ExceedingPaymentException(
                "Payment exceeds the invoice amount by 50.00. Balance amount to be paid is 50.00"))
                .when(invoiceService).payInvoice(Mockito.anyLong(), Mockito.any());

        mockMvc.perform(post("/invoices/1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":150.00}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage",
                        is("Payment exceeds the invoice amount by 50.00. Balance amount to be paid is 50.00")));
    }

    @Test
    public void processOverdueInvoices_ShouldReturnStatusOk() throws Exception {
        Mockito.doNothing().when(invoiceService).processOverdueInvoices(Mockito.any());

        mockMvc.perform(post("/invoices/process-overdue")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"overdueDays\":5, \"lateFee\":10.00}"))
                .andExpect(status().isOk());
    }

    @Test
    public void createInvoice_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        mockMvc.perform(post("/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":,\"dueDate\":\"2023-12-31\"}"))
                .andExpect(status().isBadRequest());
    }
}
