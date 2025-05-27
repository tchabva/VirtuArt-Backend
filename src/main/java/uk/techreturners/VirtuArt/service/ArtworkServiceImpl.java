package uk.techreturners.VirtuArt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.techreturners.VirtuArt.model.aicapi.AicApiResult;
import uk.techreturners.VirtuArt.repository.AicApiDAO;

@Service
public class ArtworkServiceImpl implements ArtworkService {

    @Autowired
    private AicApiDAO aicApiDAO;

    @Override
    public AicApiResult getAicArtworks(String limit, String page) {
        return aicApiDAO.getArtworks(limit, page);
    }
}
