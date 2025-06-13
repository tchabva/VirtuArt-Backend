package uk.techreturners.VirtuArt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.model.request.AdvancedSearchRequest;
import uk.techreturners.VirtuArt.repository.AicApiDAO;
import uk.techreturners.VirtuArt.util.DTOMapper;
import uk.techreturners.VirtuArt.util.SearchRequestMapper;

@Service
public class ArtworkServiceImpl implements ArtworkService, DTOMapper, SearchRequestMapper {

    @Autowired
    private AicApiDAO aicApiDAO;

    @Override
    public PaginatedArtworkResultsDTO getAicArtworks(String limit, String page) {
        return aicPaginatedResponseMapper(aicApiDAO.getArtworks(limit, page));
    }

    @Override
    public ArtworkDTO getArtworkById(String source, String artworkId) {
        switch (source){
            case "aic" -> {
                return createArtworkDtoWithAicApi(aicApiDAO.getArtworkById(artworkId).data());
            }
            default -> throw new IllegalArgumentException("Invalid data source: " + source);
        }
    }

    @Override
    public PaginatedArtworkResultsDTO getArtworksByAdvancedSearchQuery(AdvancedSearchRequest searchQuery) {
        if (searchQuery.source().equalsIgnoreCase("aic")){
            return getAicArtworksBySearchQuery(searchQuery);
        }
        return null;
    }

    @Override
    public PaginatedArtworkResultsDTO getAicArtworksBySearchQuery(AdvancedSearchRequest searchQuery) {

        return aicPaginatedResponseMapper(aicApiDAO.getArtworksByElasticSearchQuery(createAicAdvancedElasticQuery(searchQuery)));
    }
}