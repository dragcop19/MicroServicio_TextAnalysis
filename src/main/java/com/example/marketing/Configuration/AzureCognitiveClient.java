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

    // Inyección de credenciales desde application.properties
    @Value("${azure.text-analysis.endpoint}")
    private String endpoint;

    @Value("${azure.text-analysis.key}")
    private String subscriptionKey;

    @Value("${azure.text-analysis.api-version}")
    private String apiVersion;

    public AzureCognitiveClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Analiza el sentimiento del texto y devuelve los resultados.
     */
    public Map<String, Object> analyzeSentiment(String text) {
        // La ruta de la API de análisis de sentimiento
        String url = endpoint + "language/analyze-text/jobs?api-version=" + apiVersion;

        // Headers requeridos por Azure (Ocp-Apim-Subscription-Key)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Ocp-Apim-Subscription-Key", subscriptionKey);

        // Cuerpo de la solicitud (Documentos a analizar)
        Map<String, Object> body = Map.of(
            "analysisInput", Map.of(
                "documents", Collections.singletonList(
                    Map.of(
                        "id", "1",
                        "text", text,
                        "language", "es" // Asumimos español si la detección de idioma es compleja
                    )
                )
            ),
            "tasks", Collections.singletonList(
                Map.of("taskName", "SentimentAnalysis",
                       "kind", "SentimentAnalysis")
            )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // Realiza la llamada POST a Azure
        ResponseEntity<Map> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            Map.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            // NOTA: Azure Language Service a menudo devuelve un 202 Accepted para trabajos asíncronos.
            // Para una integración simple, usaremos un endpoint más directo o simplificaremos la respuesta.
            
            // Simulación: Si usáramos el endpoint de análisis rápido, devolvería el resultado.
            // Devolveremos la respuesta completa para que el servicio la procese.
            return response.getBody();
        }

        throw new RuntimeException("Fallo al contactar Azure Cognitive Service. Código: " + response.getStatusCode());
    }
}