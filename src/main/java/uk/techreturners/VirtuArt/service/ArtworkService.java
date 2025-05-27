package uk.techreturners.VirtuArt.service;

import uk.techreturners.VirtuArt.model.aicapi.AicApiResult;

public interface ArtworkService {
    AicApiResult getArtworks(String limit, String page);
}
