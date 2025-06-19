package uk.techreturners.VirtuArt.model.cmaapi;

public record CmaApiPagination(
        Integer total,
        CmaApiPaginationParameters parameters
) {
    public boolean checkHasNext() {
        Integer currentPage = parameters.calculateCurrentPage();
        Integer totalPages = parameters.calculateTotalPages(total);
        return currentPage < totalPages;
    }

    public boolean checkHasPrevious() {
        Integer currentPage = parameters.calculateCurrentPage();
        return currentPage > 1;
    }

    public record CmaApiPaginationParameters(
            Integer skip,
            Integer limit
    ) {
        // Note: to calculate the skip = currentPage-1 (from Pager or Pagination) * limit

        public Integer calculateCurrentPage() {
            return (skip / limit) + 1;
        }

        public Integer calculateTotalPages(Integer total) {
            return (int) Math.ceil((double) total / limit);
        }
    }
}