package com.example.marketing.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

// Al definirlo como 'record', Java crea el constructor automáticamente.
public record PublicationRequestDTO(

        @NotNull(message = "El ID de la campaña es obligatorio") Integer campaignId,

        @NotNull(message = "El ID del autor es obligatorio") Integer authorApiId,

        @NotBlank(message = "El contenido de texto es obligatorio") String textContent,

        @NotNull(message = "La fecha de publicación es obligatoria") OffsetDateTime publicationDate,

        @Min(0) Integer likes,

        @Min(0) Integer comments,

        @Min(0) Integer shares,

        @Size(max = 100) String geolocation,

        @Size(max = 512) String publicationUrl,

        @Size(max = 50) String classificationPriority) {
}