package uk.techreturners.VirtuArt.model.aicapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AicApiPagination(
        Integer total,
        Integer limit,
        @JsonProperty("total_pages") Integer totalPages,
        @JsonProperty("current_page") Integer currentPage
) {
    public Boolean checkHasNext(){
        return currentPage < totalPages;
    }
    public Boolean checkHasPrevious(){
        return currentPage > 1;
    }
}
