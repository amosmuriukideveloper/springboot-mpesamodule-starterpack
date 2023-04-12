package com.microservice.PaymentMpesa.Utils;

import okhttp3.MediaType;

public class PaymentModuleUtils {
    public static final String BASIC_AUTH_STRING = "Basic";
    public static final String BEARER_AUTH_STRING = "Bearer";
    public static final String AUTHORIZATION_HEADER_STRING = "authorization";
    public static final String CACHE_CONTROL_HEADER = "cache-control";
    public static final String CACHE_CONTROL_HEADER_VALUE = "no-cache";
    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    // Utility method to check if a string is null or empty
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Utility method to generate a basic authentication header
    public static String generateBasicAuthHeader(String username, String password) {
        String credentials = username + ":" + password;
        String encodedCredentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
        return BASIC_AUTH_STRING + " " + encodedCredentials;
    }

    // Utility method to generate a bearer authentication header
    public static String generateBearerAuthHeader(String token) {
        return BEARER_AUTH_STRING + " " + token;
    }

    // Utility method to generate a cache control header
    public static String generateCacheControlHeader() {
        return CACHE_CONTROL_HEADER + ": " + CACHE_CONTROL_HEADER_VALUE;
    }
}
