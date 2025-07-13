package uk.techreturners.VirtuArt.model.dto;

public record ExhibitionItemDTO(
        String id,
        String apiId,
        String title,
        String artist,
        String date,
        String imageUrl,
        String source
) {
}