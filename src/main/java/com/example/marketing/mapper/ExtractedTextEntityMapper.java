package com.example.marketing.mapper;

import com.example.marketing.dto.ExtractedTextEntityRequestDTO;
import com.example.marketing.dto.ExtractedTextEntityResponseDTO;
import com.example.marketing.model.ExtractedTextEntity;

public class ExtractedTextEntityMapper {

    private ExtractedTextEntityMapper() {
        // Clase estática de utilidad
    }


    public static ExtractedTextEntity toEntity(ExtractedTextEntityRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        // Se asume que el TextAnalysis padre será asignado por el servicio.
        
        ExtractedTextEntity entity = new ExtractedTextEntity();
        entity.setEntityText(dto.entityText());
        entity.setEntityType(dto.entityType());
        entity.setConfidenceScore(dto.confidenceScore());
        
        return entity;
    }

    public static ExtractedTextEntityResponseDTO toResponseDTO(ExtractedTextEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return ExtractedTextEntityResponseDTO.builder()
                .entityId(entity.getEntityId())
                // Asumiendo que la Entidad TextAnalysis existe y la relación está cargada
                .textAnalysisId(entity.getTextAnalysis() != null ? entity.getTextAnalysis().getTextAnalysisId() : null)
                .entityText(entity.getEntityText())
                .entityType(entity.getEntityType())
                .confidenceScore(entity.getConfidenceScore())
                .build();
    }

    public static void copyToEntity(ExtractedTextEntityRequestDTO dto, ExtractedTextEntity entity) {
        if (dto == null || entity == null) {
            return;
        }
        
        entity.setEntityText(dto.entityText());
        entity.setEntityType(dto.entityType());
        entity.setConfidenceScore(dto.confidenceScore());
        
    }
}