package uk.techreturners.VirtuArt.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchResult;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchArtwork;
import uk.techreturners.VirtuArt.model.aicapi.AicApiArtwork;
import uk.techreturners.VirtuArt.model.aicapi.AicApiPagination;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    @DisplayName("aicImageUrlCreator correctly forms URL")
    void testAicImageUrlCreator() {
        // Arrange
        String artworkImageId = "test_image_id";
        String expectedUrl = "https://www.artic.edu/iiif/2/" + artworkImageId + "/full/843,/0/default.jpg";

        // Act
        String actualUrl = aicImageUrlCreator(artworkImageId);

        // Assert
        assertEquals(expectedUrl, actualUrl, "Image URL should be correctly formatted.");
    }

    @Test
    @DisplayName("aicImageUrlCreator handles null image ID")
    void testAicImageUrlCreatorForNullImageId() {
        // Arrange
        String artworkImageId = null;
        String expectedUrl = ""; // Current behavior

        // Act
        String actualUrl = aicImageUrlCreator(artworkImageId);

        // Assert
        assertEquals(expectedUrl, actualUrl, "Image URL should handle null ID by returning empty string.");
    }

    @Test
    @DisplayName("aicImageUrlCreator handles Blank image ID")
    void testAicImageUrlCreatorForBlankImageId() {
        // Arrange
        String emptyImageId = "";
        String blankImageId = "  ";
        String expectedUrl = ""; // Current behavior

        // Act
        String actualUrl1 = aicImageUrlCreator(emptyImageId);
        String actualUrl2 = aicImageUrlCreator(emptyImageId);

        // Assert
        assertEquals(expectedUrl, actualUrl1, "Image URL should handle null ID by returning empty string.");
        assertEquals(expectedUrl, actualUrl2, "Image URL should handle null ID by returning empty string.");
    }
}