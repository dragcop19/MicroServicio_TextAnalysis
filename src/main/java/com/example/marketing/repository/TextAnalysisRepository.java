package com.example.marketing.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.marketing.model.TextAnalysis;

@Repository
public interface TextAnalysisRepository extends JpaRepository<TextAnalysis, Integer> {

    TextAnalysis findByPublicationApiId(Integer publicationApiId);

    List<TextAnalysis> findBySentiment(String sentiment);
    
    List<TextAnalysis> findBySentimentConfidenceScoreGreaterThan(BigDecimal score);
}