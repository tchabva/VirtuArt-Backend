package uk.techreturners.VirtuArt.model.dto;

import lombok.Builder;

import java.util.List;

@Builder
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