package uk.techreturners.VirtuArt.util;

import org.junit.jupiter.api.BeforeEach;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchResult;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchArtwork;
import uk.techreturners.VirtuArt.model.aicapi.AicApiArtwork;
import uk.techreturners.VirtuArt.model.aicapi.AicApiPagination;

import java.util.Arrays;

class DTOMapperTest implements DTOMapper {

    // AIC API Mocks
    private AicApiArtwork mockAicApiArtwork;
    private AicApiSearchArtwork mockAicApiSearchArtwork1;
    private AicApiSearchArtwork mockAicApiSearchArtwork2;
    private AicApiSearchResult mockAicApiSearchResult;
    private AicApiPagination mockAicApiPagination;

    @BeforeEach
    void setUp() {
        // Setup for createArtworkDtoWithAicApi
        mockAicApiArtwork = new AicApiArtwork(
                12345,
                "The Starry Night",
                "1889",
                "Saint-Rémy-de-Provence, France",
                "Painted in June 1889, it depicts the view from the east-facing window of his asylum room...",
                "Oil on canvas",
                "Post-Impressionism",
                "Vincent van Gogh",
                "starry_night_image_id",
                Arrays.asList("alt_id_1", "alt_id_2")
        );

        // Setup for aicArtworkResultsResponseMapper
        mockAicApiSearchArtwork1 = new AicApiSearchArtwork(
                101,
                "Impression, soleil levant",
                "1872",
                "Claude Monet"
        );

        // Setup for aicArtworkResultsResponseMapper
        mockAicApiSearchArtwork2 = new AicApiSearchArtwork(
                1012,
                "Water Lilies",
                "1915",
                "Claude Monet"
        );

        mockAicApiPagination = new AicApiPagination(
                200,
                10,
                20,
                2
        );
        mockAicApiSearchResult = new AicApiSearchResult(
                mockAicApiPagination,
                Arrays.asList(mockAicApiSearchArtwork1, mockAicApiSearchArtwork2)
        );

    }
}