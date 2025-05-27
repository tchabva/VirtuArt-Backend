package uk.techreturners.VirtuArt.service;

import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;

import java.util.List;

public interface ArtworkService {
    PaginatedArtworkResultsDTO getAicArtworks(String limit, String page);

}
