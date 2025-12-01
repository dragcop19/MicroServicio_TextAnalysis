package com.example.marketing.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "text_analysis")
public class TextAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "text_analysis_id")
    private Integer textAnalysisId; 

    @Column(name = "publication_api_id", nullable = false)
    private Integer publicationApiId;

    @Column(name = "sentiment", length = 20)
    private String sentiment;

    @Column(name = "sentiment_confidence_score", precision = 5, scale = 4)
    private BigDecimal sentimentConfidenceScore;

    @Column(name = "detected_language", length = 10)
    private String detectedLanguage;

    @Column(name = "crisis_score", precision = 5, scale = 2)
    private BigDecimal crisisScore;
    @Column(name = "analysis_date", nullable = false)
    private ZonedDateTime analysisDate;
    
    @OneToMany(mappedBy = "textAnalysis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExtractedTextEntity> extractedTextEntities;
}
