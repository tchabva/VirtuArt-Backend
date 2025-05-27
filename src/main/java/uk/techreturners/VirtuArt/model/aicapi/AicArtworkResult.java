package uk.techreturners.VirtuArt.model.aicapi;

public record AicArtworkResult(
        AicPagination pagination,
        AicArtwork data
) {
}
