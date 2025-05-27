package uk.techreturners.VirtuArt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.repository.AicApiDAO;

import java.util.List;

@Service
public class ArtworkServiceImpl implements ArtworkService {

    @Autowired
    private AicApiDAO aicApiDAO;

    @Override
    public List<PaginatedArtworkResultsDTO> getAicArtworks(String limit, String page) {
        return aicApiDAO.getArtworks(limit, page);
    }
}
