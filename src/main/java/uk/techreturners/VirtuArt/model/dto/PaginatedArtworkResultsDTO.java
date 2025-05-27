package uk.techreturners.VirtuArt.model.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PaginatedArtworkResultsDTO(
        List<ArtworkResultsDTO> data,
        Integer totalItems,
        Integer pageSize,
        Integer totalPages,
        Integer currentPage,
        Boolean hasNext,
        Boolean hasPrevious
) {
}
