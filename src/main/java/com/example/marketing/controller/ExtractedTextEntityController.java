package com.example.marketing.controller;

import com.example.marketing.dto.ExtractedTextEntityRequestDTO;
import com.example.marketing.dto.ExtractedTextEntityResponseDTO;
import com.example.marketing.service.ExtractedTextEntityService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/analysis/entities") // Base path para las entidades extraídas
@Tag(name = "Extracted Text Entities", description = "API for managing specific entities detected in text (e.g., names, brands)")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ExtractedTextEntityController {

    private final ExtractedTextEntityService extractedTextEntityService;

    // =======================================================
    // === CRUD Endpoints ====================================
    // =======================================================

    @Operation(summary = "Create a new extracted text entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entity created"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<ExtractedTextEntityResponseDTO> createEntity(
            @Valid @RequestBody ExtractedTextEntityRequestDTO requestDTO) {
        ExtractedTextEntityResponseDTO createdEntity = extractedTextEntityService.create(requestDTO);
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @Operation(summary = "Get an extracted entity by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity found"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExtractedTextEntityResponseDTO> getEntityById(@PathVariable Integer id) {
        ExtractedTextEntityResponseDTO entity = extractedTextEntityService.findById(id);
        return ResponseEntity.ok(entity);
    }

    @Operation(summary = "Get all extracted text entities paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of entities")
    })
    @GetMapping
    public ResponseEntity<Page<ExtractedTextEntityResponseDTO>> getAll(Pageable pageable) {
        Page<ExtractedTextEntityResponseDTO> entities = extractedTextEntityService.findAll(pageable); // Llama al nuevo método
        return ResponseEntity.ok(entities);
    }

    @Operation(summary = "Update an existing extracted entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity updated"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExtractedTextEntityResponseDTO> updateEntity(@PathVariable Integer id,
            @Valid @RequestBody ExtractedTextEntityRequestDTO requestDTO) {
        ExtractedTextEntityResponseDTO updatedEntity = extractedTextEntityService.update(id, requestDTO);
        return ResponseEntity.ok(updatedEntity);
    }

    @Operation(summary = "Delete an extracted entity by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entity deleted"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Integer id) {
        extractedTextEntityService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // =======================================================
    // === Query Endpoints ===================================
    // =======================================================

    @Operation(summary = "Find all extracted entities belonging to a specific Text Analysis ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of entities found")
    })
    @GetMapping("/analysis/{analysisId}")
    public ResponseEntity<List<ExtractedTextEntityResponseDTO>> getEntitiesByAnalysisId(
            @PathVariable Integer analysisId) {
        List<ExtractedTextEntityResponseDTO> entities = extractedTextEntityService.findEntitiesByAnalysisId(analysisId);
        return ResponseEntity.ok(entities);
    }

    @Operation(summary = "Find all extracted entities of a specific type (e.g., 'PERSON', 'LOCATION')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of entities found")
    })
    @GetMapping("/type/{entityType}")
    public ResponseEntity<List<ExtractedTextEntityResponseDTO>> getEntitiesByEntityType(
            @PathVariable String entityType) {
        List<ExtractedTextEntityResponseDTO> entities = extractedTextEntityService.findByEntityType(entityType);
        return ResponseEntity.ok(entities);
    }
}