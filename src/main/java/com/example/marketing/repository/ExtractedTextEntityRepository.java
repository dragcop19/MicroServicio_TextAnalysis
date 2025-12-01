package com.example.marketing.repository;

import com.example.marketing.model.ExtractedTextEntity;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtractedTextEntityRepository extends JpaRepository<ExtractedTextEntity, Integer> {

    List<ExtractedTextEntity> findByTextAnalysis_TextAnalysisId(Integer textAnalysisId);

    List<ExtractedTextEntity> findByEntityType(String entityType);

    List<ExtractedTextEntity> findByEntityTextContainingIgnoreCase(String entityText);
}