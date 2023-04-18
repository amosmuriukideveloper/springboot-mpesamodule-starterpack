package com.microservice.PaymentMpesa.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InternalB2CTransactionRequest {
    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("Amount")
    private String amount;

    @JsonProperty("Occasion")
    private String occasion;

    @JsonProperty("PartyB")
    private String partyB;
}
