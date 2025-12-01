package com.example.marketing.service;

import com.example.marketing.dto.PublicationResponseDTO;
import com.example.marketing.dto.TextAnalysisRequestDTO;
import com.example.marketing.dto.TextAnalysisResponseDTO;
import com.example.marketing.mapper.TextAnalysisMapper; // Se asume que existe
import com.example.marketing.model.TextAnalysis;
import com.example.marketing.repository.TextAnalysisRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TextAnalysisServiceImpl implements TextAnalysisService {

    private final TextAnalysisRepository textAnalysisRepository;
    private final RestTemplate restTemplate;

    private static final String MARKETING_CORE_URL = "http://localhost:8081/api/v1/publications/";

    // =======================================================
    // === Implementación de Métodos CRUD ====================
    // =======================================================

    @Override
    public TextAnalysisResponseDTO create(TextAnalysisRequestDTO request) {

        TextAnalysis newAnalysis = TextAnalysisMapper.toEntity(request);
        validatePublicationExistence(request.publicationApiId());

        if (newAnalysis.getPublicationApiId() == null) {
            newAnalysis.setPublicationApiId(request.publicationApiId());
        }

        if (newAnalysis.getAnalysisDate() == null) {
            newAnalysis.setAnalysisDate(ZonedDateTime.now());
        }

        TextAnalysis savedAnalysis = textAnalysisRepository.save(newAnalysis);

        return TextAnalysisMapper.toResponseDTO(savedAnalysis);
    }

    @Override
    @Transactional(readOnly = true)
    public TextAnalysisResponseDTO findById(Integer analysisId) {
        TextAnalysis analysis = textAnalysisRepository.findById(analysisId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Análisis de Texto no encontrado con ID: " + analysisId));
        return TextAnalysisMapper.toResponseDTO(analysis);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TextAnalysisResponseDTO> findAllAnalyses(Pageable pageable) {
        Page<TextAnalysis> analysisPage = textAnalysisRepository.findAll(pageable);
        return analysisPage.map(TextAnalysisMapper::toResponseDTO);
    }

    @Override
    public TextAnalysisResponseDTO update(Integer analysisId, TextAnalysisRequestDTO request) {
        TextAnalysis existingAnalysis = textAnalysisRepository.findById(analysisId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Análisis de Texto no encontrado con ID: " + analysisId));

        TextAnalysisMapper.copyToEntity(request, existingAnalysis);
        // Si hay que actualizar la publicación, se haría aquí.

        TextAnalysis savedAnalysis = textAnalysisRepository.save(existingAnalysis);
        return TextAnalysisMapper.toResponseDTO(savedAnalysis);
    }

    @Override
    public void delete(Integer analysisId) {
        if (!textAnalysisRepository.existsById(analysisId)) {
            throw new EntityNotFoundException("No se puede eliminar. Análisis no encontrado con ID: " + analysisId);
        }
        textAnalysisRepository.deleteById(analysisId);
    }

    // =======================================================
    // === Implementación de Lógica Específica ===============
    // =======================================================

    @Override
    @Transactional(readOnly = true)
    public TextAnalysisResponseDTO findByPublicationId(Integer publicationApiId) {
        TextAnalysis analysis = textAnalysisRepository.findByPublicationApiId(publicationApiId);

        if (analysis == null) {
            throw new EntityNotFoundException("Análisis no encontrado para la Publicación ID: " + publicationApiId);
        }
        return TextAnalysisMapper.toResponseDTO(analysis);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TextAnalysisResponseDTO> findBySentiment(String sentiment) {
        return textAnalysisRepository.findBySentiment(sentiment).stream()
                .map(TextAnalysisMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TextAnalysisResponseDTO> findByConfidenceAbove(BigDecimal confidenceScore) {
        return textAnalysisRepository.findBySentimentConfidenceScoreGreaterThan(confidenceScore).stream()
                .map(TextAnalysisMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PublicationResponseDTO validatePublicationExistence(Integer publicationId) {
        String url = MARKETING_CORE_URL + publicationId;

        try {
            ResponseEntity<PublicationResponseDTO> response = restTemplate.getForEntity(
                    url,
                    PublicationResponseDTO.class);

            // Si el código es 2xx, devolvemos el cuerpo
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }

        } catch (HttpClientErrorException.NotFound e) {
            throw new EntityNotFoundException(
                    "La publicación padre (ID: " + publicationId + ") no fue encontrada en Marketing Core.");

        } catch (ResourceAccessException e) {
            throw new RuntimeException("Fallo en la comunicación con Marketing. ¿Está corriendo en 8081?", e);
        }

        throw new EntityNotFoundException(
                "Error al verificar o recibir detalles de la publicación ID: " + publicationId);
    }

    public PublicationResponseDTO retrievePublicationById(Integer publicationId) {
        
        return validatePublicationExistence(publicationId);
    }
}