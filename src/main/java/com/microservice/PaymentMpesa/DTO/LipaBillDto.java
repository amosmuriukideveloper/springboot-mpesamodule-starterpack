package com.microservice.PaymentMpesa.DTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LipaBillDto {
    private String PayerNo;
    private String payBillNo;
    private float amount;
}
