package uk.techreturners.VirtuArt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.repository.AicApiDAO;
import uk.techreturners.VirtuArt.repository.CmaApiDAO;
import uk.techreturners.VirtuArt.util.DTOMapper;
import uk.techreturners.VirtuArt.util.SearchRequestMapper;

@Service
public class ArtworkServiceImpl implements ArtworkService, DTOMapper, SearchRequestMapper {

    @Autowired
    private AicApiDAO aicApiDAO;

    @Autowired
    private CmaApiDAO cmaApiDAO;

    @Override
    public PaginatedArtworkResultsDTO getArtworks(String source, String limit, String page) {
        switch (source) {
            case "aic" -> {
                return getAicArtworks(limit, page);
            }
            case "cma" -> {
                return getCmaArtworks(limit, page);
            }
            // TODO: create an illegal source exception
            case null, default -> throw new IllegalArgumentException("Invalid data source: " + source);
        }
    }

    // TODO update testing
    @Override
    public PaginatedArtworkResultsDTO getAicArtworks(String limit, String page) {
        return aicPaginatedResponseMapper(aicApiDAO.getArtworks(limit, page));
    }

    @Override
    public PaginatedArtworkResultsDTO getCmaArtworks(String limit, String page) {
        return cmaPaginatedResponseMapper(
                cmaApiDAO.getArtworks(Integer.parseInt(limit), Integer.parseInt(page))
        );
    }

    @Override
    public ArtworkDTO getArtworkById(String source, String artworkId) {
        switch (source) {
            case "aic" -> {
                return createArtworkDtoWithAicApi(aicApiDAO.getArtworkById(artworkId).data());
            }
            case "cma" -> {
                return createArtworkDtoWithCmaApi(cmaApiDAO.getArtworkById(artworkId).data());
            }
            case null, default -> throw new IllegalArgumentException("Invalid data source: " + source);
        }
    }
}