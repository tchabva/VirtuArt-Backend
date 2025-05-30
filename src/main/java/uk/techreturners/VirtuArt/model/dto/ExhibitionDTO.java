package uk.techreturners.VirtuArt.model.dto;

import lombok.Builder;
import uk.techreturners.VirtuArt.model.ExhibitionItem;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ExhibitionDTO(
        String id,
        String title,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ExhibitionItem> exhibitionItems
) {
}
