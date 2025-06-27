package uk.techreturners.VirtuArt.service;

import uk.techreturners.VirtuArt.model.aicapi.AicApiElasticSearchQuery;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.model.request.AdvancedSearchRequest;
import uk.techreturners.VirtuArt.model.request.BasicSearchRequest;

public interface SearchService {
    PaginatedArtworkResultsDTO getArtworksByAdvancedSearchQuery(AdvancedSearchRequest searchQuery);

    PaginatedArtworkResultsDTO getArtworksByBasicSearchQuery(BasicSearchRequest searchQuery);

    PaginatedArtworkResultsDTO getAicArtworksBySearchQuery(AicApiElasticSearchQuery elasticSearchQuery);
}