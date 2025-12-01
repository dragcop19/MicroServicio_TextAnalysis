package com.example.marketing.service;

import com.example.marketing.dto.ExtractedTextEntityRequestDTO;
import com.example.marketing.dto.ExtractedTextEntityResponseDTO;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExtractedTextEntityService {

    // --- Métodos CRUD Básicos ---
    
    ExtractedTextEntityResponseDTO create(ExtractedTextEntityRequestDTO request);

    ExtractedTextEntityResponseDTO findById(Integer entityId);

    Page<ExtractedTextEntityResponseDTO> findAll(Pageable pageable);

    ExtractedTextEntityResponseDTO update(Integer entityId, ExtractedTextEntityRequestDTO request);

    void delete(Integer entityId);

    // --- Métodos de Búsqueda Específica ---
    
    List<ExtractedTextEntityResponseDTO> findEntitiesByAnalysisId(Integer textAnalysisId);
    
    List<ExtractedTextEntityResponseDTO> findByEntityType(String entityType);
}