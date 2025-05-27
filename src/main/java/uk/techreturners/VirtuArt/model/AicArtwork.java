package uk.techreturners.VirtuArt.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AicArtwork(
        Integer id,
        String title,
        @JsonProperty("date_display") String dateDisplay,
        @JsonProperty("place_of_origin") String origin,
        String description,
        @JsonProperty("medium_display") String displayMedium,
        @JsonProperty("department_title") String department,
        @JsonProperty("artist_title") String artist,
        @JsonProperty("image_id") String primaryImageId,
        @JsonProperty("alt_image_ids") List<String> altImageIds
) {
}
