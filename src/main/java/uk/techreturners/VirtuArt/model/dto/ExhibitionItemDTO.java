package uk.techreturners.VirtuArt.model.dto;

import lombok.Builder;

@Builder
public record ExhibitionItemDTO(
        String id,
        String apiId,
        String title,
        String date,
        String imageUrl,
        String source
) {
}