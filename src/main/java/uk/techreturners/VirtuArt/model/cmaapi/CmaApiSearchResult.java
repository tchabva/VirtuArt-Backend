package uk.techreturners.VirtuArt.model.cmaapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CmaApiSearchResult(
        CmaApiPagination info,
        List<CmaApiSearchArtwork> data
) {
    public record CmaApiSearchArtwork(
            Long id,
            String title,
            @JsonProperty("creation_date") String creationDate,
            CmaApiImages images,
            List<CmaApiCreators> creators
    ){
    }
}