package com.example.marketing.service;

import com.example.marketing.dto.ExtractedTextEntityRequestDTO;
import com.example.marketing.dto.ExtractedTextEntityResponseDTO;
import com.example.marketing.mapper.ExtractedTextEntityMapper; // Se asume que existe
import com.example.marketing.model.ExtractedTextEntity;
import com.example.marketing.model.TextAnalysis;
import com.example.marketing.repository.ExtractedTextEntityRepository;
// import com.example.Marketing.model.TextAnalysis;
// import com.example.Marketing.repository.TextAnalysisRepository;
import com.example.marketing.repository.TextAnalysisRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ExtractedTextEntityServiceImpl implements ExtractedTextEntityService {

    private final ExtractedTextEntityRepository entityRepository;
    private final TextAnalysisRepository textAnalysisRepository; // Necesario si la lógica de negocio requiere buscar el padre

    // =======================================================
    // === Implementación de Métodos CRUD ====================
    // =======================================================

    @Override
    public ExtractedTextEntityResponseDTO create(ExtractedTextEntityRequestDTO request) {
        
        // 1. BUSCAR LA ENTIDAD PADRE OBLIGATORIA (CORRECCIÓN CRÍTICA)
        TextAnalysis parentAnalysis = textAnalysisRepository.findById(request.textAnalysisId())
             .orElseThrow(() -> new EntityNotFoundException("Análisis padre no encontrado con ID: " + request.textAnalysisId()));
        
        ExtractedTextEntity newEntity = ExtractedTextEntityMapper.toEntity(request);
        
        newEntity.setTextAnalysis(parentAnalysis);
        
        ExtractedTextEntity savedEntity = entityRepository.save(newEntity);
        return ExtractedTextEntityMapper.toResponseDTO(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public ExtractedTextEntityResponseDTO findById(Integer entityId) {
        ExtractedTextEntity entity = entityRepository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("Entidad de Texto no encontrada con ID: " + entityId));
        return ExtractedTextEntityMapper.toResponseDTO(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    // Acepta Pageable y retorna Page<DTO>
    public Page<ExtractedTextEntityResponseDTO> findAll(Pageable pageable) {         
        Page<ExtractedTextEntity> entityPage = entityRepository.findAll(pageable);
        return entityPage.map(ExtractedTextEntityMapper::toResponseDTO);
    }

    @Override
    public ExtractedTextEntityResponseDTO update(Integer entityId, ExtractedTextEntityRequestDTO request) {
        ExtractedTextEntity existingEntity = entityRepository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("Entidad de Texto no encontrada con ID: " + entityId));

        if (!existingEntity.getTextAnalysis().getTextAnalysisId().equals(request.textAnalysisId())) {
            TextAnalysis parentAnalysis = textAnalysisRepository.findById(request.textAnalysisId())
                 .orElseThrow(() -> new EntityNotFoundException("Análisis padre no encontrado con ID: " + request.textAnalysisId()));
            existingEntity.setTextAnalysis(parentAnalysis);
        }

        ExtractedTextEntityMapper.copyToEntity(request, existingEntity);

        ExtractedTextEntity savedEntity = entityRepository.save(existingEntity);
        return ExtractedTextEntityMapper.toResponseDTO(savedEntity);
    }

    @Override
    public void delete(Integer entityId) {
        if (!entityRepository.existsById(entityId)) {
            throw new EntityNotFoundException("No se puede eliminar. Entidad no encontrada con ID: " + entityId);
        }
        entityRepository.deleteById(entityId);
    }
    
    // =======================================================
    // === Implementación de Lógica Específica ===============
    // =======================================================

    @Override
    @Transactional(readOnly = true)
    public List<ExtractedTextEntityResponseDTO> findEntitiesByAnalysisId(Integer textAnalysisId) {
        return entityRepository.findByTextAnalysis_TextAnalysisId(textAnalysisId).stream()
                .map(ExtractedTextEntityMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExtractedTextEntityResponseDTO> findByEntityType(String entityType) {
        return entityRepository.findByEntityType(entityType).stream()
                .map(ExtractedTextEntityMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}