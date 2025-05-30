package uk.techreturners.VirtuArt.service;

import uk.techreturners.VirtuArt.model.aicapi.AicApiElasticSearchQuery;
import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;

public interface ArtworkService {
    PaginatedArtworkResultsDTO getAicArtworks(String limit, String page);
    ArtworkDTO getArtworkById(String source, String artworkId);
    PaginatedArtworkResultsDTO getAicArtworksBySearchQuery(AicApiElasticSearchQuery searchQuery);
}