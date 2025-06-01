package uk.techreturners.VirtuArt.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ExhibitionDetailDTO(
        String id,
        String title,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ExhibitionItemDTO> exhibitionItems
) {
}