package uk.techreturners.VirtuArt.service;

import org.springframework.security.oauth2.jwt.Jwt;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDTO;
import uk.techreturners.VirtuArt.model.dto.ExhibitionDetailDTO;
import uk.techreturners.VirtuArt.model.request.AddArtworkRequest;
import uk.techreturners.VirtuArt.model.request.CreateExhibitionRequest;
import uk.techreturners.VirtuArt.model.request.UpdateExhibitionRequest;

import java.util.List;

public interface ExhibitionService {

    List<ExhibitionDTO> getAllUserExhibitions(Jwt jwt);

    ExhibitionDetailDTO getExhibitionById(String id, Jwt jwt);

    ExhibitionDTO createUserExhibition(CreateExhibitionRequest request, Jwt jwt);

    ExhibitionDTO addArtworkToExhibition(String exhibitionId, AddArtworkRequest request, Jwt jwt);

    Void removeArtworkFromExhibition(String exhibitionId, String apiId, String source, Jwt jwt);

    Void deleteExhibition(String id, Jwt jwt);

    ExhibitionDTO updateExhibitionDetails(String exhibitionId, UpdateExhibitionRequest request, Jwt jwt);
}