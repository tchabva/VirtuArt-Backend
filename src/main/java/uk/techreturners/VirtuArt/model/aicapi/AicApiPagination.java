package uk.techreturners.VirtuArt.model.aicapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AicApiPagination(
        Integer total,
        Integer limit,
        Integer offset,
        @JsonProperty("total_pages") Integer totalPages,
        @JsonProperty("current_page") Integer currentPage,
        @JsonProperty("next_url") String nextUrl
) {
}
