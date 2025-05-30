package uk.techreturners.VirtuArt.model.dto;

import lombok.Builder;
import uk.techreturners.VirtuArt.model.ExhibitionItem;

import java.util.List;

@Builder
public record ExhibitionDTO(
        String id,
        String title,
        String description,
        String createdAt,
        String updatedAt,
        List<ExhibitionItem> exhibitionItems
) {
}
