package uk.techreturners.VirtuArt.service;

import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;

public interface ArtworkService {
    PaginatedArtworkResultsDTO getAicArtworks(String limit, String page);
}
