package com.microservice.PaymentMpesa.Controller;

import com.microservice.PaymentMpesa.DTO.PaymentRequestDTO;
import com.microservice.PaymentMpesa.DTO.PaymentResponseDTO;
import com.microservice.PaymentMpesa.Services.PaymentModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentModuleService paymentModuleService;

    // Endpoint for initiating a payment
    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponseDTO> initiatePayment(@RequestBody PaymentRequestDTO paymentRequest) throws IOException, IOException {
        PaymentResponseDTO response = paymentModuleService.processMPESAResponse(paymentRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint for handling MPESA API response callback
    @PostMapping("/callback")
    public ResponseEntity<PaymentResponseDTO> handleMPESAResponse(@RequestBody PaymentRequestDTO paymentResponse) {
        PaymentResponseDTO response = paymentModuleService.processMPESAResponseAndUpdateSystem(paymentResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}