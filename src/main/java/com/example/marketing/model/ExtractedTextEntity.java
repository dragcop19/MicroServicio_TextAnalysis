package com.example.marketing.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "extracted_text_entities")
public class ExtractedTextEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entity_id")
    private Integer entityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "text_analysis_id", nullable = false)
    private TextAnalysis textAnalysis;

    @Column(name = "entity_text", length = 255, nullable = false)
    private String entityText;

    @Column(name = "entity_type", length = 50)
    private String entityType;

    @Column(name = "confidence_score", precision = 5, scale = 4)
    private BigDecimal confidenceScore;
}
