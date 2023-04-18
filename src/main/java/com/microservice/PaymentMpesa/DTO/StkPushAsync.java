package com.microservice.PaymentMpesa.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.StreamingHttpOutputMessage;


@Data
public class StkPushAsync {
    @JsonProperty("Body")
    private StreamingHttpOutputMessage.Body body;
}
