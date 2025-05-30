package uk.techreturners.VirtuArt.model.dto;

public record AddArtworkRequest(
        String apiId,
        String title,
        String artistTitle,
        String date,
        String imageUrl,
        String source
) {
}
