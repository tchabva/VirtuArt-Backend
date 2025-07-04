package uk.techreturners.VirtuArt.model.dto;

public record ArtworkResultsDTO(
        String id,
        String title,
        String artistTitle,
        String date,
        String imageURL,
        String source
) {
}