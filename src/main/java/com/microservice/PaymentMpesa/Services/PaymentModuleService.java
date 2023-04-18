package com.microservice.PaymentMpesa.Services;

import com.microservice.PaymentMpesa.DTO.ExternalStkPushRequest;
import com.microservice.PaymentMpesa.DTO.PaymentRequestDTO;
import com.microservice.PaymentMpesa.DTO.StkPushSyncResponse;
import com.microservice.PaymentMpesa.Utils.Constants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

import static com.microservice.PaymentMpesa.Utils.Constants.*;

@Service
public class PaymentModuleService {
    // Implement initiatePayment method to initiate payment using MPESA API

    public PaymentRequestDTO processMPESAResponse(PaymentRequestDTO paymentResponse) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        MediaType mediaType = MediaType.parseMediaType("application/json");

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json"), "{\"BusinessShortCode\": 174379, " +
                "\"Password\": \"MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMjMwNDExMTExNDI4\", " +
                "\"Timestamp\": \"20230411111428\", " +
                "\"TransactionType\": " +
                "\"CustomerPayBillOnline\", " +
                "\"Amount\": 1, "                                                                                                                                                                                                                                                                            +
                "\"PartyA\": 254743888952, " +
                "\"PartyB\": 174379, " +
                "\"PhoneNumber\": 254743888952, " +
                "\"CallBackURL\": \"https://mydomain.com/path\", " +
                "\"AccountReference\": " +
                "\"CompanyXLTD\", " +
                "\"TransactionDesc\": " +
                "\"Payment of X\"}");

        Request request = new Request.Builder()

                .url("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")

                .method("POST", body)

                .addHeader("Content-Type", "application/json")

                .addHeader("Authorization", "Bearer EPpLgDzxDBQUX6de56j65zltaAqz")

                .build();

        Response response = client.newCall(request).execute();
        return paymentResponse;
    }


    @Override
    public StkPushSyncResponse performStkPushTransaction(InternalStkPushRequest internalStkPushRequest) {
        ExternalStkPushRequest externalStkPushRequest = new ExternalStkPushRequest();
        externalStkPushRequest.setBusinessShortCode(mpesaConfiguration.getStkPushShortCode());

        String transactionTimestamp = HelperUtility.getTransactionTimestamp();
        String stkPushPassword = HelperUtility.getStkPushPassword(mpesaConfiguration.getStkPushShortCode(),
                mpesaConfiguration.getStkPassKey(), transactionTimestamp);

        externalStkPushRequest.setPassword(stkPushPassword);
        externalStkPushRequest.setTimestamp(transactionTimestamp);
        externalStkPushRequest.setTransactionType(Constants.CUSTOMER_PAYBILL_ONLINE);
        externalStkPushRequest.setAmount(internalStkPushRequest.getAmount());
        externalStkPushRequest.setPartyA(internalStkPushRequest.getPhoneNumber());
        externalStkPushRequest.setPartyB(mpesaConfiguration.getStkPushShortCode());
        externalStkPushRequest.setPhoneNumber(internalStkPushRequest.getPhoneNumber());
        externalStkPushRequest.setCallBackURL(mpesaConfiguration.getStkPushRequestCallbackUrl());
        externalStkPushRequest.setAccountReference(HelperUtility.getTransactionUniqueNumber());
        externalStkPushRequest.setTransactionDesc(String.format("%s Transaction", internalStkPushRequest.getPhoneNumber()));

        AccessTokenResponse accessTokenResponse = getAccessToken();

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE,
                Objects.requireNonNull(HelperUtility.toJson(externalStkPushRequest)));

        Request request = new Request.Builder()
                .url(mpesaConfiguration.getStkPushRequestUrl())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();


        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
            // use Jackson to Decode the ResponseBody ...

            return objectMapper.readValue(response.body().string(), StkPushSyncResponse.class);
        } catch (IOException e) {
            log.error(String.format("Could not perform the STK push request -> %s", e.getLocalizedMessage()));
            return null;
        }

    }


    // Process MPESA API response and update system
    public PaymentRequestDTO processMPESAResponseAndUpdateSystem(PaymentRequestDTO paymentResponse) {
        // Update payment status in your system
        updatePaymentStatus(String.valueOf(paymentResponse.getId()), paymentResponse.getStatus());

        // Check payment status and perform actions accordingly
        String paymentStatus = paymentResponse.getStatus();
        switch (paymentStatus) {
            case "success":
                // Perform actions for successful payment
                // ...

                break;
            case "failure":
                // Perform actions for failed payment
                // ...
                logFailureReason(paymentResponse.getFailureReason());
                sendPaymentFailureNotification(paymentStatus, paymentResponse.getFailureReason());
                break;
            default:
                // Log unknown payment status
                logUnknownPaymentStatus("Unknown payment status: " + paymentStatus);
                break;
        }
        // Return updated payment response
        return paymentResponse;
    }
    private void logApiCallFailure(String errorMessage) {
        System.out.println("Failure Reason: " + errorMessage);
    }

    private void logInvalidResponse(String errorMessage) {
        System.out.println("Failure Reason: " + errorMessage);

    }

    private void sendPaymentStatusUpdate(String paymentStatus, String errorMessage) {
        System.out.println("Payment Failed: " + paymentStatus + ", Reason: " + errorMessage);

    }

    private void logUnknownPaymentStatus(String errorMessage) {
        System.out.println("Unknown payment  " + errorMessage);

    }

    // Placeholder methods for performing actions with failure details and unknown payment status

    private void updatePaymentStatus(String paymentReference, String paymentStatus) {
        // Update payment status in your system
        System.out.println("Payment Status Update: Reference: " + paymentReference + ", Status: " + paymentStatus);
        // ...
    }

    private void logFailureReason(String failureReason) {
        // Log failure reason in your system
        System.out.println("Failure Reason: " + failureReason);
        // ...
    }

    private void sendPaymentFailureNotification(String paymentStatus, String failureReason) {
        // Send payment failure notification to user
        System.out.println("Payment Failed: " + paymentStatus + ", Reason: " + failureReason);
        // ...
    }

    private void sendNotification(String errorMessage) {
        System.out.println("Failure Reason: " + errorMessage);

    }
}


