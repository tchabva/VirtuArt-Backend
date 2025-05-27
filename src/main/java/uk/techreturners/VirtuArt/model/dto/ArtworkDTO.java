package uk.techreturners.VirtuArt.model.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ArtworkDTO(
        String id,
        String title,
        String artist,
        String date,
        String displayMedium,
        String imageUrl,
        List<String> altImageUrls,
        String description,
        String origin,
        String category,
        String sourceMuseum
) {
}
