package com.microservice.PaymentMpesa.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO extends PaymentResponseDTO {
    private Long id;
    private String phoneNumber;
    private double amount;
    private String status;
    private String reference;
    private String failureReason;
    private String logUnknownPaymentStatus;
    private String sendPaymentStatusUpdate;

    // String-argument constructor
    public PaymentRequestDTO(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}
