package uk.techreturners.VirtuArt.model.dto;

import java.util.List;

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
        String department,
        String sourceMuseum
) {
}