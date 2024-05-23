package com.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PayOSService {

    private final String API_URL = "https://api.payos.com/transaction";

    public String performPayment(Map<String, Object> paymentDetails) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, paymentDetails, String.class);
        return response.getBody();
    }
}