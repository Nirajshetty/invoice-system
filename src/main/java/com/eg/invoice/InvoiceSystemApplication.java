package com.eg.invoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Invoice System REST API Documentation", description = "Invoice System REST API Documentation that consists APIs that allows creating invoices, paying invoices, and processing overdue invoices", version = "v1", contact = @Contact(name = "Niraj Shetty", email = "nirajshetty2000@gmail.com", url = "https://www.linkedin.com/in/nirajrshetty")))
public class InvoiceSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceSystemApplication.class, args);
	}
}
