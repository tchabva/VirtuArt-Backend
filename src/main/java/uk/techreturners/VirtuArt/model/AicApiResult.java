package uk.techreturners.VirtuArt.model;

import java.util.List;

// The Art Institute of Chicago API
public record AicApiResult(
        AicPagination pagination,
        List<AicArtworkSearchResult> data
) {
}
