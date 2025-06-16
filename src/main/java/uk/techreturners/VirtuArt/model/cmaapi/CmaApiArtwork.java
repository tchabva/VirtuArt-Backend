package uk.techreturners.VirtuArt.model.cmaapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CmaApiArtwork(
        Long id,
        String title,
        @JsonProperty("creation_date") String creationDate,
        List<String> culture,
        String technique,
        String department,
        String description,
        CmaApiImages images,
        @JsonProperty("alternate_images") List<CmaApiAlternateImages> alternateImages,
        List<CmaApiCreators> creators
) {
    public record CmaApiAlternateImages(
            CmaApiImages.CmaApiWeb web
    ) {
    }
}
