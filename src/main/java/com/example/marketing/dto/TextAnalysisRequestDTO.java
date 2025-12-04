package com.example.marketing.dto;

import jakarta.validation.constraints.NotNull;


public record TextAnalysisRequestDTO(

         @NotNull(message = "El ID de la publicación es obligatorio") 
         Integer publicationApiId
) {
}