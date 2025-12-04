package com.example.marketing.mapper;

import com.example.marketing.dto.ExtractedTextEntityResponseDTO;
import com.example.marketing.dto.TextAnalysisRequestDTO;
import com.example.marketing.dto.TextAnalysisResponseDTO;
import com.example.marketing.model.TextAnalysis;

import java.time.OffsetDateTime;

// Importar el mapper de entidades hijas para el DTO de respuesta

import java.util.List;
import java.util.stream.Collectors;

public class TextAnalysisMapper {

    private TextAnalysisMapper() {
        
    }


    public static TextAnalysis toEntity(TextAnalysisRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        TextAnalysis entity = new TextAnalysis();
        
        entity.setPublicationApiId(dto.publicationApiId());
 
        return entity;
    }

    public static TextAnalysisResponseDTO toResponseDTO(TextAnalysis entity) {
        if (entity == null) {
            return null;
        }

        List<ExtractedTextEntityResponseDTO> entities = entity.getExtractedTextEntities() != null
                ? entity.getExtractedTextEntities().stream()
                        .map(ExtractedTextEntityMapper::toResponseDTO) 
                        .collect(Collectors.toList())
                : null;
        
        return TextAnalysisResponseDTO.builder()
                .textAnalysisId(entity.getTextAnalysisId())
                .publicationApiId(entity.getPublicationApiId())
                .sentiment(entity.getSentiment())
                .sentimentConfidenceScore(entity.getSentimentConfidenceScore())
                .detectedLanguage(entity.getDetectedLanguage())
                .crisisScore(entity.getCrisisScore())
                .analysisDate(entity.getAnalysisDate() != null ? OffsetDateTime.from(entity.getAnalysisDate()) : null) 
                .extractedEntities(entities)
                .build();
    }

    public static void copyToEntity(TextAnalysisRequestDTO dto, TextAnalysis entity) {
        if (dto == null || entity == null) {
            return;
        }
    }
}