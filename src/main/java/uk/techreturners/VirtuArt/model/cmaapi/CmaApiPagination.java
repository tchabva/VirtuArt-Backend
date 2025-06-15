package uk.techreturners.VirtuArt.model.cmaapi;

public record CmaApiPagination(
        Integer total,
        CmaApiPaginationParameters parameters
) {
    private record CmaApiPaginationParameters(
            Integer skip,
            Integer limit
    ){
        // Note: to calculate the skip = currentPage-1 (from Pager or Pagination) * limit
    }
}