package com.example.marketing.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ExtractedTextEntityRequestDTO(

        @NotNull(message = "El ID del análisis padre es obligatorio")
        Integer textAnalysisId, 

        @NotBlank(message = "El texto de la entidad es obligatorio")
        @Size(max = 255)
        String entityText,

        @Size(max = 50)
        String entityType,

        @NotNull(message = "La puntuación de confianza es obligatoria")
        @DecimalMin(value = "0.0000", inclusive = true)
        @DecimalMax(value = "1.0000", inclusive = true)
        BigDecimal confidenceScore) {
}