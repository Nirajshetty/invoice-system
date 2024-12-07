package com.eg.invoice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Schema(name = "CreateInvoiceResponse", description = "Schema to hold Invoice creation response")
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceResponseDTO {

        @Schema(description = "Unique identifier for the created invoice", example = "12345", type = "integer", format = "int64")
        @NonNull
        private Long id;
}
