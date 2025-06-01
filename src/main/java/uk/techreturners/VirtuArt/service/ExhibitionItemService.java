package uk.techreturners.VirtuArt.service;

import uk.techreturners.VirtuArt.model.ExhibitionItem;
import uk.techreturners.VirtuArt.model.request.CreateExhibitionRequest;

public interface ExhibitionItemService {
    ExhibitionItem addNewExhibitionItem(CreateExhibitionRequest request);
    ExhibitionItem getOrCreateExhibitionItem(String apiID, String source);
}
