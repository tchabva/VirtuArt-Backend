package uk.techreturners.VirtuArt.model.dto;

import java.util.List;

public record PaginatedArtworkResultsDTO(
        Integer totalItems,
        Integer pageSize,
        Integer totalPages,
        Integer currentPage,
        Boolean hasNext,
        Boolean hasPrevious,
        List<ArtworkResultsDTO> data
) {
}