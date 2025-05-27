package uk.techreturners.VirtuArt.model.aicapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AicArtworkSearchResult(
        Integer id,
        String title,
        @JsonProperty("date_display") String dateDisplay,
        @JsonProperty("artist_title") String artistTitle
) {
}
