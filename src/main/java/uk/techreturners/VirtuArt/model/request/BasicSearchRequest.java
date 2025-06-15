package uk.techreturners.VirtuArt.model.request;

public record BasicSearchRequest(
        String query,
        String source,
        Integer pageSize,
        Integer currentPage
) {
}
