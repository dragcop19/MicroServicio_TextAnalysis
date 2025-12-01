package com.example.marketing.service;

import com.example.marketing.dto.PublicationResponseDTO;
import com.example.marketing.dto.TextAnalysisRequestDTO;
import com.example.marketing.dto.TextAnalysisResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface TextAnalysisService {

    // --- Métodos CRUD Básicos ---

    TextAnalysisResponseDTO create(TextAnalysisRequestDTO request);

    TextAnalysisResponseDTO findById(Integer analysisId);

    //List<TextAnalysisResponseDTO> findAll();

    Page<TextAnalysisResponseDTO> findAllAnalyses(Pageable pageable);

    TextAnalysisResponseDTO update(Integer analysisId, TextAnalysisRequestDTO request);

    void delete(Integer analysisId);

    // --- Métodos de Búsqueda y Lógica Específica ---
    
    // Busca el análisis por el ID de la publicación asociada
    PublicationResponseDTO validatePublicationExistence(Integer publicationApiId);
    TextAnalysisResponseDTO findByPublicationId(Integer publicationApiId); 
    
    // Encuentra los análisis con un sentimiento específico (e.g., 'Negative')
    List<TextAnalysisResponseDTO> findBySentiment(String sentiment);
    
    // Encuentra análisis con una confianza de sentimiento superior a un umbral
    List<TextAnalysisResponseDTO> findByConfidenceAbove(BigDecimal confidenceScore);
}