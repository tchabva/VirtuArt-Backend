package uk.techreturners.VirtuArt.model.aicapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AicApiPagination(
        Integer total,
        Integer limit,
        @JsonProperty("total_pages") Integer totalPages,
        @JsonProperty("current_page") Integer currentPage
) {
}
