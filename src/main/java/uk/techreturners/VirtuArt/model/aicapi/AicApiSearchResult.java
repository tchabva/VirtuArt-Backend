package uk.techreturners.VirtuArt.model.aicapi;

import java.util.List;

// The Art Institute of Chicago API
public record AicApiSearchResult(
        AicApiPagination pagination,
        List<AicApiSearchArtwork> data
) {
}
