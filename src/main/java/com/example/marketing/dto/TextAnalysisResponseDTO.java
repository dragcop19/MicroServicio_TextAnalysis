package com.example.marketing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextAnalysisResponseDTO {

    @JsonProperty("analysis_id")
    private Integer textAnalysisId;
    
    @JsonProperty("publication_id")
    private Integer publicationApiId;

    @JsonProperty("sentiment")
    private String sentiment;

    @JsonProperty("sentiment_confidence")
    private BigDecimal sentimentConfidenceScore;

    @JsonProperty("detected_language")
    private String detectedLanguage;
    
    @JsonProperty("crisis_score")
    private BigDecimal crisisScore;
    
    @JsonProperty("analysis_date")
    private OffsetDateTime analysisDate;

    @JsonProperty("extracted_entities")
    private List<ExtractedTextEntityResponseDTO> extractedEntities;
}