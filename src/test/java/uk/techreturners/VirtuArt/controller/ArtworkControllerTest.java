package uk.techreturners.VirtuArt.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.techreturners.VirtuArt.model.dto.ArtworkResultsDTO;
import uk.techreturners.VirtuArt.model.dto.PaginatedArtworkResultsDTO;
import uk.techreturners.VirtuArt.service.ArtworkService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class ArtworkControllerTest {

    @Mock
    private ArtworkService mockArtworkService;

    @InjectMocks
    private ArtworkController artworkController;

    private PaginatedArtworkResultsDTO expectedPaginatedResponse;

    @BeforeEach
    void setUp() {
        // Arrange: Common setup for multiple tests
        ArtworkResultsDTO artworkDTO = ArtworkResultsDTO.builder()
                .id("1")
                .title("Test Artwork")
                .artistTitle("Test Artist")
                .date("2023")
                .build();
        List<ArtworkResultsDTO> artworkList = Collections.singletonList(artworkDTO);

        expectedPaginatedResponse = PaginatedArtworkResultsDTO.builder()
                .data(artworkList)
                .totalItems(1)
                .pageSize(10)
                .totalPages(1)
                .currentPage(1)
                .hasNext(false)
                .hasPrevious(false)
                .build();
    }

    @Test
    @DisplayName("getArtworks with default parameters returns PaginatedArtworkResultsDTO and OK status")
    void testGetAicArtworksWithDefaultParametersReturnsPaginatedResultsAndOk() {
        // Arrange
        String source = "aic";
        String defaultLimit = "20";
        String defaultPage = "1";
        when(mockArtworkService.getArtworks(source, defaultLimit, defaultPage)).thenReturn(expectedPaginatedResponse);

        // Act
        ResponseEntity<PaginatedArtworkResultsDTO> responseEntity = artworkController.getArtworks(source, defaultLimit, defaultPage);

        // Assert
        assertAll(
                () -> assertNotNull(responseEntity),
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(expectedPaginatedResponse, responseEntity.getBody())
        );
        verify(mockArtworkService, times(1)).getArtworks(source, defaultLimit, defaultPage);
    }

    @Test
    @DisplayName("getArtworks with custom parameters returns PaginatedArtworkResultsDTO and OK status")
    void testGetAicArtworksCustomParametersReturnsPaginatedResultsAndOk() {
        // Arrange
        String source = "aic";
        String customLimit = "10";
        String customPage = "2";
        when(mockArtworkService.getArtworks(source, customLimit, customPage)).thenReturn(expectedPaginatedResponse);

        // Act
        ResponseEntity<PaginatedArtworkResultsDTO> responseEntity = artworkController.getArtworks(source, customLimit, customPage);

        // Assert
        assertAll(
                () -> assertNotNull(responseEntity),
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(expectedPaginatedResponse, responseEntity.getBody())
        );
        verify(mockArtworkService, times(1)).getArtworks(source, customLimit, customPage);
    }

    @Test
    @DisplayName("getArtworks when service returns empty data list")
    void testGetAicArtworksServiceReturnsEmptyDataReturnsPaginatedResultsAndOk() {
        // Arrange
        String source = "aic";
        String limit = "5";
        String page = "1";
        PaginatedArtworkResultsDTO emptyDataResponse = PaginatedArtworkResultsDTO.builder()
                .data(Collections.emptyList())
                .totalItems(0)
                .pageSize(5)
                .totalPages(0)
                .currentPage(1)
                .hasNext(false)
                .hasPrevious(false)
                .build();
        when(mockArtworkService.getArtworks(source, limit, page)).thenReturn(emptyDataResponse);

        // Act
        ResponseEntity<PaginatedArtworkResultsDTO> responseEntity = artworkController.getArtworks(source, limit, page);

        // Assert
        assertAll(
                () -> assertNotNull(responseEntity),
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> {
                    assertNotNull(responseEntity.getBody());
                    assertEquals(0, responseEntity.getBody().data().size());
                },
                () -> assertEquals(emptyDataResponse, responseEntity.getBody())
        );
        verify(mockArtworkService,times(1)).getArtworks(source, limit, page);
    }
}