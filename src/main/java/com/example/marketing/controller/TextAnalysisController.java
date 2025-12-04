package com.example.marketing.controller;

import com.example.marketing.dto.PublicationResponseDTO;
import com.example.marketing.dto.TextAnalysisRequestDTO;
import com.example.marketing.dto.TextAnalysisResponseDTO;
import com.example.marketing.service.TextAnalysisService;
import com.example.marketing.service.TextAnalysisServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analysis/text")
@Tag(name = "Text Analysis", description = "API for managing the cognitive analysis results of publications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TextAnalysisController {

    private final TextAnalysisService textAnalysisService;

    // =======================================================
    // === CRUD Endpoints ====================================
    // =======================================================

    @Operation(summary = "Create a new text analysis result (e.g., from an AI service response)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Analysis result created"),
            @ApiResponse(responseCode = "400", description = "Invalid request or publication already analyzed")
    })
    @PostMapping
    public ResponseEntity<TextAnalysisResponseDTO> createTextAnalysis(
            @Valid @RequestBody TextAnalysisRequestDTO requestDTO) {
        TextAnalysisResponseDTO createdAnalysis = textAnalysisService.create(requestDTO);
        return new ResponseEntity<>(createdAnalysis, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a text analysis result by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analysis found"),
            @ApiResponse(responseCode = "404", description = "Analysis not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TextAnalysisResponseDTO> getTextAnalysisById(@PathVariable Integer id) {
        TextAnalysisResponseDTO analysis = textAnalysisService.findById(id);
        return ResponseEntity.ok(analysis);
    }

    @Operation(summary = "Get all text analysis results with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of analysis results"),
            @ApiResponse(responseCode = "404", description = "No analyses found")
    })
    @GetMapping
    public ResponseEntity<Page<TextAnalysisResponseDTO>> getAllTextAnalyses(Pageable pageable) {
        Page<TextAnalysisResponseDTO> analyses = textAnalysisService.findAllAnalyses(pageable);
        return ResponseEntity.ok(analyses);
    }

    @Operation(summary = "Update an existing text analysis result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analysis updated"),
            @ApiResponse(responseCode = "404", description = "Analysis not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TextAnalysisResponseDTO> updateTextAnalysis(@PathVariable Integer id,
            @Valid @RequestBody TextAnalysisRequestDTO requestDTO) {
        TextAnalysisResponseDTO updatedAnalysis = textAnalysisService.update(id, requestDTO);
        return ResponseEntity.ok(updatedAnalysis);
    }

    @Operation(summary = "Delete a text analysis result by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Analysis deleted"),
            @ApiResponse(responseCode = "404", description = "Analysis not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTextAnalysis(@PathVariable Integer id) {
        textAnalysisService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // =======================================================
    // === Query Endpoints ===================================
    // =======================================================

    @Operation(summary = "Get text analysis result by its associated Publication ID (1:1 relationship)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analysis found"),
            @ApiResponse(responseCode = "404", description = "Analysis not found for this publication")
    })
    @GetMapping("/publication/{publicationId}")
    public ResponseEntity<TextAnalysisResponseDTO> getTextAnalysisByPublicationId(@PathVariable Integer publicationId) {
        TextAnalysisResponseDTO analysis = textAnalysisService.findByPublicationId(publicationId);
        return ResponseEntity.ok(analysis);
    }

    @Operation(summary = "Find all analyses with a specific sentiment (e.g., 'Negative')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of analyses found")
    })
    @GetMapping("/search/sentiment/{sentiment}")
    public ResponseEntity<List<TextAnalysisResponseDTO>> getAnalysesBySentiment(@PathVariable String sentiment) {
        List<TextAnalysisResponseDTO> analyses = textAnalysisService.findBySentiment(sentiment);
        return ResponseEntity.ok(analyses);
    }

    @Operation(summary = "Find analyses where sentiment confidence is above a specified score")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of high-confidence analyses found")
    })
    @GetMapping("/search/confidence")
    public ResponseEntity<List<TextAnalysisResponseDTO>> getAnalysesByConfidenceAbove(@RequestParam BigDecimal score) {
        List<TextAnalysisResponseDTO> analyses = textAnalysisService.findByConfidenceAbove(score);
        return ResponseEntity.ok(analyses);
    }
    // En TextAnalysisController.java (Microservicio de Análisis de Texto)

    @Operation(summary = "Retrieve publication details from Marketing Core for context")
    @GetMapping("/external-publication/{publicationId}")
    public ResponseEntity<PublicationResponseDTO> getExternalPublicationDetails(
            @PathVariable Integer publicationId) {

        PublicationResponseDTO publication = textAnalysisService.retrievePublicationById(publicationId);
        return ResponseEntity.ok(publication);
    }
}