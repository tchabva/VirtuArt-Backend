package uk.techreturners.VirtuArt.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchResult;
import uk.techreturners.VirtuArt.model.aicapi.AicApiSearchArtwork;
import uk.techreturners.VirtuArt.model.aicapi.AicApiArtwork;
import uk.techreturners.VirtuArt.model.aicapi.AicApiPagination;
import uk.techreturners.VirtuArt.model.dto.ArtworkDTO;
import uk.techreturners.VirtuArt.model.dto.ArtworkResultsDTO;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;

import java.util.Arrays;
import java.util.Collections;

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

    @Test
    @DisplayName("createArtworkDtoWithAicApi maps AicArtwork to ArtworkDTO correctly")
    void testCreateArtworkDtoWithAicApi() {
        // Act
        ArtworkDTO artworkDTO = createArtworkDtoWithAicApi(mockAicApiArtwork);

        // Assert
        assertAll("createArtworkDtoWithAicApi returns a correctly mapped ArtworkDTO",
                () -> assertNotNull(artworkDTO),
                () -> assertEquals(String.valueOf(mockAicApiArtwork.id()), artworkDTO.id()),
                () -> assertEquals(mockAicApiArtwork.title(), artworkDTO.title()),
                () -> assertEquals(mockAicApiArtwork.artist(), artworkDTO.artist()),
                () -> assertEquals(mockAicApiArtwork.dateDisplay(), artworkDTO.date()),
                () -> assertEquals(mockAicApiArtwork.displayMedium(), artworkDTO.displayMedium()),
                () -> assertEquals(mockAicApiArtwork.description(), artworkDTO.description()),
                () -> assertEquals(mockAicApiArtwork.department(), artworkDTO.category()),
                () -> assertEquals(mockAicApiArtwork.origin(), artworkDTO.origin()),
                () -> assertEquals(
                        aicImageUrlCreator(mockAicApiArtwork.primaryImageId()), artworkDTO.imageUrl()
                ),
                () -> assertEquals("Art Institute of Chicago", artworkDTO.sourceMuseum()),
                () -> assertNotNull(mockAicApiArtwork.altImageIds()),
                () -> assertEquals(
                        mockAicApiArtwork.altImageIds().size(), artworkDTO.altImageUrls().size()
                )
        );

        if (!mockAicApiArtwork.altImageIds().isEmpty()) {
            assertEquals(
                    aicImageUrlCreator(mockAicApiArtwork.altImageIds().get(0)),
                    artworkDTO.altImageUrls().getFirst(),
                    "First alt image URL should match."
            );
        }
    }

    @Test
    @DisplayName("aicArtworkResultsResponseMapper maps AicApiSearchArtwork to ArtworkResultsDTO correctly")
    void testAicSearchArtworkResultsResponseMapper() {
        // Act
        ArtworkResultsDTO artworkResultsDTO = aicSearchArtworkResultsResponseMapper(mockAicApiSearchArtwork1);

        // Assert
        assertAll("Maps ArtworkResultsDTO from AicApiSearchArtwork",
                () -> assertNotNull(artworkResultsDTO),
                () -> assertEquals(mockAicApiSearchArtwork1.id().toString(), artworkResultsDTO.id()),
                () -> assertEquals(mockAicApiSearchArtwork1.title(), artworkResultsDTO.title()),
                () -> assertEquals(mockAicApiSearchArtwork1.artistTitle(), artworkResultsDTO.artistTitle()),
                () -> assertEquals(mockAicApiSearchArtwork1.dateDisplay(), artworkResultsDTO.date())
        );
    }

    @Test
    @DisplayName("aicPaginatedResponseMapper maps AicApiResult to PaginatedArtworkResultsDTO correctly")
    void testAicPaginatedResponseMapper() {
        // Act
        PaginatedArtworkResultsDTO paginatedDTO = aicPaginatedResponseMapper(mockAicApiSearchResult);

        // Assert
        assertAll("Maps AicApiSearchResult to PaginatedArtworkResultsDTO",
                () -> assertNotNull(paginatedDTO),
                () -> assertEquals(
                        mockAicApiSearchResult.pagination().total(),
                        paginatedDTO.totalItems()),
                () -> assertEquals(
                        mockAicApiSearchResult.pagination().limit(),
                        paginatedDTO.pageSize()),
                () -> assertEquals(
                        mockAicApiSearchResult.pagination().totalPages(), paginatedDTO.totalPages()),
                () -> assertEquals(
                        mockAicApiSearchResult.pagination().currentPage(), paginatedDTO.currentPage()
                ),
                () -> assertTrue(paginatedDTO.hasNext()),
                () -> assertTrue(paginatedDTO.hasPrevious()),
                () -> assertNotNull(paginatedDTO.data()),
                () -> assertEquals(mockAicApiSearchResult.data().size(), paginatedDTO.data().size())
        );

        // Check mapping of individual artwork results
        if (!paginatedDTO.data().isEmpty()) {
            ArtworkResultsDTO firstArtworkResult = paginatedDTO.data().getFirst();
            AicApiSearchArtwork firstApiArtwork = mockAicApiSearchResult.data().getFirst();
            assertEquals(String.valueOf(firstApiArtwork.id()), firstArtworkResult.id());
            assertEquals(firstApiArtwork.title(), firstArtworkResult.title());
        }
    }

    @Test
    @DisplayName("aicPaginatedResponseMapper handles empty data list from API result")
    void testAicPaginatedResponseMapper_emptyData() {
        // Arrange
        AicApiSearchResult emptyDataApiResult = new AicApiSearchResult(
                mockAicApiPagination, // Use the same pagination
                Collections.emptyList()
        );

        // Act
        PaginatedArtworkResultsDTO paginatedDTO = aicPaginatedResponseMapper(emptyDataApiResult);

        // Assert
        assertAll("aicPaginatedResponse Mapper handles empty data list from API result",
                () -> assertNotNull(paginatedDTO),
                () -> assertNotNull(paginatedDTO.data()),
                () -> assertTrue(paginatedDTO.data().isEmpty()),
                () -> assertEquals(mockAicApiPagination.total(), paginatedDTO.totalItems())
        );
    }
}