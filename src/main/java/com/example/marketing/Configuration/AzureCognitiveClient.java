package com.example.marketing.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Component
public class AzureCognitiveClient {

    private final RestTemplate restTemplate;

    // Inyección de credenciales
    @Value("${azure.text-analysis.endpoint}")
    private String endpoint;

    @Value("${azure.text-analysis.key}")
    private String subscriptionKey;

    public AzureCognitiveClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> analyzeSentiment(String text) {
        String url = endpoint + "text/analytics/v3.1/sentiment"; 

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Ocp-Apim-Subscription-Key", subscriptionKey);

        Map<String, Object> body = Map.of(
            "documents", Collections.singletonList(
                Map.of(
                    "id", "1",
                    "text", text,
                    "language", "es"
                )
            )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            Map.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }

        throw new RuntimeException("Fallo al contactar Azure Cognitive Service. Código: " + response.getStatusCode());
    }
}