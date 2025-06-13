package uk.techreturners.VirtuArt.service;

import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.model.request.AdvancedSearchRequest;

public interface ArtworkService {
    PaginatedArtworkResultsDTO getAicArtworks(String limit, String page);
    ArtworkDTO getArtworkById(String source, String artworkId);
    PaginatedArtworkResultsDTO getArtworksByAdvancedSearchQuery(AdvancedSearchRequest searchQuery);
    PaginatedArtworkResultsDTO getAicArtworksBySearchQuery(AdvancedSearchRequest searchQuery);
}