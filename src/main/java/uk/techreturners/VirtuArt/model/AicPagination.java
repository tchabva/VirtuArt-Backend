package uk.techreturners.VirtuArt.model;

public record AicPagination(
        Integer total,
        Integer limit,
        Integer offset,
        Integer total_pages,
        Integer current_page,
        String next_url
) {
}
