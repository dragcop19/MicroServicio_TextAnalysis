package com.example.marketing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtractedTextEntityResponseDTO {

    @JsonProperty("entity")
    private Integer entityId;
    
    @JsonProperty("text analysis")
    private Integer textAnalysisId; 

    @JsonProperty("entity text")
    private String entityText;

    @JsonProperty("entity type")
    private String entityType;
    
    @JsonProperty("confidence score")
    private BigDecimal confidenceScore;
}