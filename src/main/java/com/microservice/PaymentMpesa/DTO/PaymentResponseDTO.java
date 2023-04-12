package com.microservice.PaymentMpesa.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {
    private String paymentId;
    private String message;
    private String status;
    private String reference;
    private String failureReason;
    private String logUnknownPaymentStatus;
    private String sendPaymentStatusUpdate;


}