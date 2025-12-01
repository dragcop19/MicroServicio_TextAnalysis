package com.example.marketing.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

// Define los datos requeridos para crear o actualizar un TextAnalysis.
public record TextAnalysisRequestDTO(
        
        // El ID de la publicación es necesario para enlazar el análisis 1:1
        @NotNull(message = "El ID de la publicación es obligatorio") 
        Integer publicationApiId, 

        @NotBlank(message = "El sentimiento es obligatorio")
        @Size(max = 20) 
        String sentiment,

        @NotNull(message = "La puntuación de confianza es obligatoria")
        @DecimalMin(value = "0.0000", inclusive = true)
        @DecimalMax(value = "1.0000", inclusive = true)
        BigDecimal sentimentConfidenceScore,

        @Size(max = 10) 
        String detectedLanguage,
        
        @NotNull(message = "El score de crisis es obligatorio")
        @DecimalMin(value = "0.00", inclusive = true)
        @DecimalMax(value = "100.00", inclusive = true)
        BigDecimal crisisScore
        
        // analysisDate no se incluye ya que se genera en el servidor
) {
}