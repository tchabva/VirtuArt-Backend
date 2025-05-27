package uk.techreturners.VirtuArt.service;

import uk.techreturners.VirtuArt.model.aicapi.AicApiResult;

public interface ArtworkService {
    AicApiResult getAicArtworks(String limit, String page);
}
