package uk.techreturners.VirtuArt.service;

import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDTO;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDetailDTO;
import uk.techreturners.VirtuArt.model.request.AddArtworkRequest;
import uk.techreturners.VirtuArt.model.request.CreateExhibitionRequest;
import uk.techreturners.VirtuArt.model.request.UpdateExhibitionRequest;

public interface ExhibitionService {

    ExhibitionDTO getAllUserExhibitions();
    ExhibitionDetailDTO getExhibitionById(String id);
    ExhibitionDTO createUserExhibition(CreateExhibitionRequest request);
    ExhibitionItem addArtworkToExhibition(String exhibitionId, AddArtworkRequest request);
    Void removeArtworkFromExhibition(String exhibitionId, String artworkId);
    Void deleteExhibition(String id);
    ExhibitionDTO updateExhibitionDetails(String exhibitionId, UpdateExhibitionRequest request);
}
