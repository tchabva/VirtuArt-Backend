package uk.techreturners.VirtuArt.model.dto;

import java.time.LocalDateTime;

public record ExhibitionDTO(
        String id,
        String title,
        Integer itemCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}