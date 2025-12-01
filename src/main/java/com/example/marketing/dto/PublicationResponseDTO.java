package com.example.marketing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder // <-- ¡Esta es la anotación que faltaba y soluciona el error!
@NoArgsConstructor
@AllArgsConstructor
public class PublicationResponseDTO {

    @JsonProperty("identifier publication api")
    private Integer publicationApiId;

    @JsonProperty("campaign")
    private Integer campaignId;

    @JsonProperty("campaign name")
    private String campaignName;

    @JsonProperty("author api")
    private Integer authorApiId;

    @JsonProperty("author username")
    private String authorUsername;

    @JsonProperty("text content")
    private String textContent;

    @JsonProperty("publication date")
    private OffsetDateTime publicationDate;

    @JsonProperty("likes")
    private Integer likes;

    @JsonProperty("comments")
    private Integer comments;

    @JsonProperty("shares")
    private Integer shares;

    @JsonProperty("publication Url")
    private String publicationUrl;

    @JsonProperty("classification Priority")
    private String classificationPriority;

    @JsonProperty("collection Date")
    private OffsetDateTime collectionDate;
}