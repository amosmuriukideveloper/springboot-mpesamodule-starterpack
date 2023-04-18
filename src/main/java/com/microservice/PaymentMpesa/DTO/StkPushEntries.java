package com.microservice.PaymentMpesa.DTO;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class StkPushEntries {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long internalId;

    @Column(unique = true)
    private String transactionId;

    private String transactionType;

    private String msisdn;

    private Long amount;

    @Column(unique = true)
    private String merchantRequestID;

    @Column(unique = true)
    private String checkoutRequestID;

    private Date entryDate;

    private String resultCode;

    private String rawCallbackPayloadResponse;


}
