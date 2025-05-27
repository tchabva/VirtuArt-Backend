package uk.techreturners.VirtuArt.model.dto;

import lombok.Builder;

@Builder
public record ArtworkResultsDTO(
        Integer id,
        String title,
        String artistTitle,
        String date
) {
}
