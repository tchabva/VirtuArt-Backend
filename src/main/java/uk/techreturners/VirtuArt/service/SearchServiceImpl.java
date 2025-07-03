package uk.techreturners.VirtuArt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.techreturners.VirtuArt.model.aicapi.AicApiElasticSearchQuery;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.model.request.AdvancedSearchRequest;
import uk.techreturners.VirtuArt.model.request.BasicSearchRequest;
import uk.techreturners.VirtuArt.repository.AicApiDAO;
import uk.techreturners.VirtuArt.repository.CmaApiDAO;
import uk.techreturners.VirtuArt.util.DTOMapper;
import uk.techreturners.VirtuArt.util.SearchRequestMapper;

@Service
public class SearchServiceImpl implements SearchService, DTOMapper, SearchRequestMapper {

    @Autowired
    private AicApiDAO aicApiDAO;

    @Autowired
    private CmaApiDAO cmaApiDAO;

    @Override
    public PaginatedArtworkResultsDTO getArtworksByAdvancedSearchQuery(AdvancedSearchRequest searchQuery) {
        if (searchQuery.source().equalsIgnoreCase("aic")) {
            return getAicArtworksBySearchQuery(createAicAdvancedElasticQuery(searchQuery));
        } else {
            throw new IllegalArgumentException(
                    "The source ".concat(searchQuery.source()).concat(" could not be matched")
            );
        }
    }

    @Override
    public PaginatedArtworkResultsDTO getArtworksByBasicSearchQuery(BasicSearchRequest searchQuery) {
        switch (searchQuery.source()) {
            case "aic" -> {
                return getAicArtworksBySearchQuery(createBasicElasticQuery(searchQuery));
            }
            case "cma" -> {
                return cmaPaginatedResponseMapper(cmaApiDAO.basicSearchQuery(searchQuery));
            }
            case null, default -> throw new IllegalArgumentException("Invalid basicSearch: " + searchQuery);
        }
    }

    @Override
    public PaginatedArtworkResultsDTO getAicArtworksBySearchQuery(AicApiElasticSearchQuery elasticSearchQuery) {
        return aicPaginatedResponseMapper(aicApiDAO.getArtworksByElasticSearchQuery(elasticSearchQuery));
    }
}