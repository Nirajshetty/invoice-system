package com.eg.invoice.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum PaymentStatus {
    @Schema(description = "Payment is pending.")
    PENDING,

    @Schema(description = "Payment has been completed.")
    PAID,

    @Schema(description = "Payment has been voided.")
    VOID
}
