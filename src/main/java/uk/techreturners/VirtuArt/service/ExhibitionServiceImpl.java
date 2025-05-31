package uk.techreturners.VirtuArt.service;

import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDTO;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDetailDTO;
import uk.techreturners.VirtuArt.model.request.AddArtworkRequest;
import uk.techreturners.VirtuArt.model.request.CreateExhibitionRequest;
import uk.techreturners.VirtuArt.model.request.UpdateExhibitionRequest;
import uk.techreturners.VirtuArt.util.DTOMapper;

public class ExhibitionServiceImpl implements ExhibitionService, DTOMapper {
    @Override
    public ExhibitionDTO getAllUserExhibitions() {
        return null;
    }

    @Override
    public ExhibitionDetailDTO getExhibitionById(String id) {
        return null;
    }

    @Override
    public ExhibitionDTO createUserExhibition(CreateExhibitionRequest request) {
        return null;
    }

    @Override
    public ExhibitionItem addArtworkToExhibition(String exhibitionId, AddArtworkRequest request) {
        return null;
    }

    @Override
    public Void removeArtworkFromExhibition(String exhibitionId, String artworkId) {
        return null;
    }

    @Override
    public Void deleteExhibition(String id) {
        return null;
    }

    @Override
    public ExhibitionDTO updateExhibitionDetails(String exhibitionId, UpdateExhibitionRequest request) {
        return null;
    }
}
