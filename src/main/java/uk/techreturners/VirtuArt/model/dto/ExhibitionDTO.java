package uk.techreturners.VirtuArt.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExhibitionDTO(
        String id,
        String title,
        Integer itemCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}