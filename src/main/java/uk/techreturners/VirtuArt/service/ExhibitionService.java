package uk.techreturners.VirtuArt.service;

import uk.techreturners.VirtuArt.model.dto.ExhibitionDTO;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDetailDTO;
import uk.techreturners.VirtuArt.model.request.AddArtworkRequest;
import uk.techreturners.VirtuArt.model.request.CreateExhibitionRequest;
import uk.techreturners.VirtuArt.model.request.UpdateExhibitionRequest;

import java.util.List;

public interface ExhibitionService {

    List<ExhibitionDTO> getAllUserExhibitions();

    ExhibitionDetailDTO getExhibitionById(String id);

    ExhibitionDTO createUserExhibition(CreateExhibitionRequest request);

    ExhibitionDTO addArtworkToExhibition(String exhibitionId, AddArtworkRequest request);

    Void removeArtworkFromExhibition(String exhibitionId, String apiId, String source);

    Void deleteExhibition(String id);

    ExhibitionDTO updateExhibitionDetails(String exhibitionId, UpdateExhibitionRequest request);
}