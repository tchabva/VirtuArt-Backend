package uk.techreturners.VirtuArt.service;

import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.request.AddArtworkRequest;
import uk.techreturners.VirtuArt.model.request.CreateExhibitionRequest;

public interface ExhibitionItemService {
    ExhibitionItem addNewExhibitionItem(AddArtworkRequest request);
    ExhibitionItem getOrCreateExhibitionItem(AddArtworkRequest request);
}
