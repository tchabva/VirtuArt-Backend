package uk.techreturners.VirtuArt.service;

import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;

public interface ArtworkService {

    PaginatedArtworkResultsDTO getArtworks(String source, String limit, String page);

    PaginatedArtworkResultsDTO getAicArtworks(String limit, String page);

    PaginatedArtworkResultsDTO getCmaArtworks(String limit, String page);

    ArtworkDTO getArtworkById(String source, String artworkId);
}