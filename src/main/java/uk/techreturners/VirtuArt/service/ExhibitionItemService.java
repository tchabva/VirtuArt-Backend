package uk.techreturners.VirtuArt.service;

import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.request.AddArtworkRequest;

public interface ExhibitionItemService {
    ExhibitionItem addNewExhibitionItem(ArtworkDTO artworkDTO, String source);
    ExhibitionItem getExhibitionItem(String apiId, String source);
    ExhibitionItem getOrCreateExhibitionItem(AddArtworkRequest request);
}