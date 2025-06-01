package uk.techreturners.VirtuArt.model.dto;

import lombok.Builder;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

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
    public static BiPredicate<Integer, Integer> checkHasNext = (cPage, tPage) -> cPage < tPage;
    public static Predicate<Integer> checkHasPrevious = cPage -> cPage > 1;
}