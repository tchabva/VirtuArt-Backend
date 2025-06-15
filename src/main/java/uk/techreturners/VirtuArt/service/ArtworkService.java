package uk.techreturners.VirtuArt.service;

import uk.techreturners.VirtuArt.model.aicapi.AicApiElasticSearchQuery;
import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.model.request.AdvancedSearchRequest;
import uk.techreturners.VirtuArt.model.request.BasicSearchRequest;

public interface ArtworkService {

    PaginatedArtworkResultsDTO getArtworks(String source, String limit, String page);

    PaginatedArtworkResultsDTO getAicArtworks(String limit, String page);

    ArtworkDTO getArtworkById(String source, String artworkId);

    PaginatedArtworkResultsDTO getArtworksByAdvancedSearchQuery(AdvancedSearchRequest searchQuery);

    PaginatedArtworkResultsDTO getArtworksByBasicSearchQuery(BasicSearchRequest searchQuery);

    PaginatedArtworkResultsDTO getAicArtworksBySearchQuery(AicApiElasticSearchQuery elasticSearchQuery);
}